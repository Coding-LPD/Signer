var fs = require('fs');
var path = require('path');
var multipart = require('connect-multiparty');
var express = require('express');
var router = express.Router();
var moment = require('moment');

var handleErrors = require('../services/error-handler').handleErrors;
var sendInfo = require('../services/error-handler').sendInfo; 
var errorCodes = require('../services/error-codes').errorCodes;
var common = require('../services/common');
var config = require('../config');
var multipartMiddleware = multipart(config.cmConfig);
var log = require('../services/log');

var Student = require('../services/mongo').Student;
var SignStudent = require('../services/mongo').SignStudent;
var Course = require('../services/mongo').Course;
var Sign = require('../services/mongo').Sign;
var SignRecord = require('../services/mongo').SignRecord;
var ChatMsg = require('../services/mongo').ChatMsg;

router.get('/', function (req, res) {
  Student.find(function (err, students) {
    if (!err) {
      sendInfo(errorCodes.Success, res, students);
    } else {
      handleErrors(err, res, []);
    }
  });
});

router.put('/:id', function (req, res) {
  var student;

  // 禁止修改学生手机号码和id
  delete req.body._id;  
  delete req.body.phone;

  Student.findByIdAndUpdate(req.params['id'], req.body, { new: true })
    .then(function (savedStudent) {
      student = savedStudent;
      
      // 修改学生的签到记录相应姓名和头像，以及发言的姓名和头像
      var promises = [];      
      var obj1 = {}, obj2 = {};
      if (req.body.name) {
        obj1.studentName = req.body.name;
        obj2.name = req.body.name;
      }
      if (req.body.avatar) {
        obj1.studentAvatar = req.body.avatar;
        obj2.avatar = req.body.avatar;
      }
      promises.push(SignRecord.update({ studentId: savedStudent._id }, obj1, { multi: true }));
      promises.push(ChatMsg.update({ studentId: studentId }, obj2, { multi: true }));
      return Promise.all(promises);
    }) 
    .then(function () {
      sendInfo(errorCodes.Success, res, student);
    })
    .catch(function (err) {
      if (err.code) {
        sendInfo(err.code, res, {});
      } else {
        handleErrors(err, res, {});
      }
    });
});

router.post('/search', function (req, res) {
  // 必须要有查询条件
  if (common.isEmptyObject(req.body)) {
    sendInfo(errorCodes.SearchEmpty, res);
    return;
  }
  Student.find(req.body, function (err, students) {
    if (!err) {
      sendInfo(errorCodes.Success, res, students);
    } else {
      handleErrors(err, res, []);
    }
  });
});

// 上传头像
router.post('/images', multipartMiddleware, function (req, res) {
  var file;
  for (var prop in req.files) {
    file = req.files[prop];
    break;
  }
  if (!file) {
    sendInfo(errorCodes.NoFileError, res, '');
    return;
  }

  var extension = file.originalFilename.slice(file.originalFilename.lastIndexOf('.'));
  var fileName = common.generateRandomStr(3) + extension; 
  // 用path.resolve来解决windonws和linux路径不同的问题
  var newPath = path.resolve(__dirname, '..' + config.userImagesPath + '/' + fileName);      
  fs.rename(file.path, newPath, function (err) {
    if (!err) {
      var url = config.userImagesUrlPrefix + fileName;
      sendInfo(errorCodes.Success, res, url);
    } else {
      handleErrors(err, res, '');
    } 
  });
});

// 获取默认头像url
router.get('/images/:id', function (req, res) {
  var id = req.params['id'];
  if (id < 1 || id > config.defaultImagesCount) {
    sendInfo(errorCodes.NoDefaultImageError, res, '');   
  } else {
    sendInfo(errorCodes.Success, res, config.userImagesUrlPrefix + id + '.png');
  }
});

// 查询学生相关课程以及课程的最近一次签到
router.get('/:phone/relatedCourses', function (req, res) {
  var phone = req.params['phone'];
  var limit = +req.query['limit'] || 10;
  var page = +req.query['page'] || 0;
  var keyword = req.query['keyword'] || '';
  var maxAvatarNum = 6;  // 每个课程最近签到的前6个学生头像
 
  // 查询该学生
  Student.find({ phone })
    .then(function (students) {
      if (students.length <= 0) {
        return Promise.reject({ code: errorCodes.UserNotExist });
      }

      var student = students[0];
      // 查询该学生的相关课程（有签到过的）
      return SignRecord.aggregate()
        .match({ studentId: '' + student._id })
        .sort('-createdAt')
        .project('courseId')
        .group({ _id: '$courseId' })
        .skip(page * limit)
        .limit(limit)      
        .exec()
    })
    .then(function (courseIdObjects) {
      // 查询该学生的相关课程的最近的一次签到
      return Promise.all(courseIdObjects.map(function (courseIdObject) {
        return Sign.aggregate()
          .match({ courseId: '' + courseIdObject._id, courseName: { $regex: keyword } })
          .sort('-createdAt')
          .project('_id courseName beforeSignIn afterSignIn courseId')
          .limit(1)
          .exec();
      }));
    })
    .then(function (results) {
      // 提取出该学生的相关课程（有签到过的）最近的一次签到
      var coursesLastestSign = results.map(function (r) {
        return r[0];
      });
      // 查询这些签到最近的签到学生的头像
      var promises = [];
      coursesLastestSign.forEach(function (sign) {
        // 某些指定id的课程，如果有关键词搜索，则可能会得不到相应签到信息，导致sign为undefined
        if (sign) {
          promises.push(SignRecord.find({ signId: sign._id, state: { $gt: 0 } }, 'studentAvatar', { sort: '-createdAt', limit: maxAvatarNum }));
        }          
      });
      // 将相关课程与签到信息传递给下一个流程
      promises.push(coursesLastestSign);
      return Promise.all(promises);
    })
    .then(function (results) {
      // 提取相关课程与签到信息
      var coursesLastestSign = results.splice(-1, 1);
      coursesLastestSign = coursesLastestSign[0].map(function (r) {
        return r;
      });
      // 拼装返回值
      var retDatas = results.map(function (r, index) {
        // 提取签到的学生头像
        var avatars = r.map(function (v) {
          return v.studentAvatar;
        });
        return {
          name: coursesLastestSign[index].courseName,
          number: coursesLastestSign[index].beforeSignIn + coursesLastestSign[index].afterSignIn,
          courseId: coursesLastestSign[index].courseId,
          avatars: avatars
        };
      });
      sendInfo(errorCodes.Success, res, retDatas);
    })
    .catch(function (err) {
      if (err.code) {
        sendInfo(err.code, res, []);
      } else {
        handleErrors(err, res, []);
      }
    });
});

// 查询学生相关通知
router.get('/:phone/notice', function (req, res) {
  var student, recordGroups;
  var type = +req.query['type'] || 0;
  var page = +req.query['page'] || 0;
  var limit = +req.query['limit'] || 10;  

  // 查询该学生
  Student.find({ phone: req.params['phone'] })
    .then(function (students) {
      if (students.length <= 0) {
        return Promise.reject({ code: errorCodes.UserNotExist })
      }

      student = students[0];
      // 查询该学生相关的已完成的签到，并根据签到id进行分组
      return SignRecord.aggregate()
        .match({ studentId: '' + student._id, type: type, state: { $gt: 0 } })
        .sort('-confirmAt')
        .skip(page * limit)
        .limit(limit)
        .project('state distance createdAt confirmAt signId')
        .group({ _id: '$signId', records: { $push: '$$ROOT' } })
        .exec();
    })
    .then(function (results) {
      recordGroups = results;

      // 查找签到记录对应的签到信息
      return Promise.all(results.map(function (result) {
        return Sign.findById({ _id: result._id }, 'courseName beforeSignIn afterSignIn');
      }));
    }) 
    .then(function (signs) {      
      var retData = [];
      recordGroups.forEach(function (group, index) {
        group.records.forEach(function (record) {
          retData.push({
            courseName: signs[index].get('courseName'),
            signState: record.state,
            signDistance: record.distance,
            signNumber: signs[index].getSignIn(type),
            signAt: record.createdAt,
            confirmAt: record.confirmAt
          });
        });
      });
      retData.sort(function (a, b) { return a.confirmAt < b.confirmAt; });
      sendInfo(errorCodes.Success, res, retData);
    })
    .catch(function (err) {
      if (err.code) {
        sendInfo(err.code, res, []);
      } else {
        handleErrors(err, res, []);
      }
    });
});

// 通过学号，关联学生个人信息和教师导入课程的学生信息，返回学生姓名和昵称
router.post('/relatedInfo', function (req, res) {
  var signId = req.body.signId;
  var number = req.body.number;
  var promises = [];

  // 查询签到信息
  Sign.findById(signId)
    .then(function (sign) {
      if (!sign) {
        return Promise.reject({ code: errorCodes.SignNotExist });
      }

      // 查新该学生个人信息以及关联的签到学生的信息
      promises.push(Student.findOne({ number: number }));
      promises.push(SignStudent.findOne({ number: number, courseId: sign.get('courseId')}));
      return Promise.all(promises);
    })
    .then(function (results) {
      var student = results[0];
      var signStudent = results[1];
      var retData = {
        name: signStudent ? signStudent.get('name') : '',
        nickname: student ? student.get('name') : ''     
      };      
      sendInfo(errorCodes.Success, res, retData);
    })
    .catch(function (err) {
      if (err.code) {
        sendInfo(err.code, res, []);
      } else {
        handleErrors(err, res, []);
      }
    });
});

// 签到数和发言数
router.get('/:id/activeInfo', function (req, res) {
  var studentId = req.params['id'];
  var promises = [];

  promises.push(ChatMsg.count({ studentId: studentId }));
  promises.push(SignRecord.aggregate()
                    .match({ studentId: studentId, state: { $gt: 0 } })
                    .group({ _id: '$signId' })
                    .exec());
  Promise.all(promises)
    .then(function (results) {
      var retData = {
        msgCount: results[0],
        signCount: results[1].length
      };
      sendInfo(errorCodes.Success, res, retData);
    })
    .catch(function (err) {
      if (err.code) {
        sendInfo(err.code, res, {});
      } else {
        handleErrors(err, res, {});
      }
    });
});

// 查询学生指定年月的有签到的日期
router.get('/:id/signInDays', function (req, res) {
  var studentId = req.params['id'];
  var date = req.query['date'] || '';  // 2016-07  
  var startTime = moment(date).format('YYYY-MM-DD HH:mm:ss');
  var endTime = moment(date).add(1, 'months').format('YYYY-MM-DD HH:mm:ss');

  Promise.resolve()
    .then(function () {
      // 查询日期为空
      if (!date.trim()) {
        return Promise.reject({ code: errorCodes.SearchDateEmpty });        
      }

      return SignRecord.find({ studentId: studentId, type: 0, confirmAt: { $gte: startTime, $lt: endTime } });
    })
    .then(function (records) {
      // 筛选出所有不同的完成签到的日期
      var day;
      var temp = {};      
      var retData = [];
      records.forEach(function (record) {
         day = moment(record.get('confirmAt')).format('YYYY-MM-DD');
         if (!temp[day]) {
           temp[day] = true;
           retData.push(day);
         }
      }); 
      sendInfo(errorCodes.Success, res, retData);
    })
    .catch(function (err) {
      if (err.code) {
        sendInfo(err.code, res, []);
      } else {
        handleErrors(err, res, []);
      }
    });
});

// 查询指定日期内的所有完成的签到
router.get('/:id/signInDays/detail', function (req, res) {
  var studentId = req.params['id'];
  var date = req.query['date'] || '';  // 2016-10-22
  var signWithRecords = [];

  Promise.resolve()
    .then(function () {
      // 查询日期为空
      if (!date.trim()) {
        return Promise.reject({ code: errorCodes.SearchDateEmpty });
      }

      var startTime = moment(date).format('YYYY-MM-DD HH:mm:ss');
      var endTime = moment(date).add(1, 'days').format('YYYY-MM-DD HH:mm:ss');
      
      // 按照签到id进行分组
      return SignRecord.aggregate()
        .match({ studentId: studentId, type: 0, confirmAt: { $gte: startTime, $lt: endTime } })
        .group({ _id: '$signId', records: { $push: '$$ROOT' } })
        .exec()
    })
    .then(function (results) {
      signWithRecords = results;

      // 查询每个签到对应的课程名称
      return Promise.all(results.map(function (r) {
        return Sign.findById(r._id);
      }));
    })
    .then(function (signs) {
      var retData = [];
      signWithRecords.forEach(function (value, index) {
        value.records.forEach(function (record) {
          retData.push({
            confirmAt: record.confirmAt,
            courseName: signs[index].get('courseName')
          });
        });
      });
      sendInfo(errorCodes.Success, res, retData);
    })
    .catch(function (err) {
      if (err.code) {
        sendInfo(err.code, res, []);
      } else {
        handleErrors(err, res, []);
      }
    });
});

// 查询学生指定年月的发言的日期
router.get('/:id/chatDays', function (req, res) {
  var studentId = req.params['id'];
  var date = req.query['date'] || '';  // 2016-10

  Promise.resolve()
    .then(function () {
      // 查询日期为空
      if (!date.trim()) {
        return Promise.reject({ code: errorCodes.SearchDateEmpty });
      }

      var startTime = moment(date).format('YYYY-MM-DD HH:mm:ss');
      var endTime = moment(date).add(1, 'months').format('YYYY-MM-DD HH:mm:ss');

      return ChatMsg.find({ studentId: studentId, createdAt: { $gte: startTime, $lt: endTime } });
    })
    .then(function (msgs) {
      // 筛选出所有不同的发言日期
      var day;
      var temp = {};
      var retData = [];
      msgs.forEach(function (msg) {
        day = moment(msg.get('createdAt')).format('YYYY-MM-DD');
        if (!temp[day]) {
          temp[day] = true;
          retData.push(day);
        }
      });
      sendInfo(errorCodes.Success, res, retData);
    })
    .catch(function (err) {
      if (err.code) {
        sendInfo(err.code, res, []);
      } else {
        handleErrors(err, res, []);
      }
    });
});

// 查询指定日期内的所有发言
router.get('/:id/chatDays/detail', function (req, res) {
  var studentId = req.params['id'];
  var date = req.query['date'] || '';  // 2016-10-22
  var roomWithMsgs = [];

  Promise.resolve()
    .then(function () {
      // 查询日期为空
      if (!date.trim()) {
        return Promise.reject({ code: errorCodes.SearchDateEmpty });
      }

      var startTime = moment(date).format('YYYY-MM-DD HH:mm:ss');
      var endTime = moment(date).add(1, 'days').format('YYYY-MM-DD HH:mm:ss');
      
      // 按照课程id进行分组
      return ChatMsg.aggregate()
        .match({ studentId: studentId, createdAt: { $gte: startTime, $lt: endTime } })
        .group({ _id: '$courseId', msgs: { $push: '$$ROOT' } })
        .exec()
    })
    .then(function (results) {
      roomWithMsgs = results;

      // 查询每个聊天室的名称
      return Promise.all(results.map(function (r) {
        return Course.findById(r._id);
      }));
    })
    .then(function (courses) {
      var retData = [];
      roomWithMsgs.forEach(function (value, index) {
        retData.push({
          courseName: courses[index].get('name'),
          msgCount: value.msgs.length
        });
      });
      sendInfo(errorCodes.Success, res, retData);
    })
    .catch(function (err) {
      if (err.code) {
        sendInfo(err.code, res, []);
      } else {
        handleErrors(err, res, []);
      }
    });
});

module.exports = router;