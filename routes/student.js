var fs = require('fs');
var path = require('path');
var multipart = require('connect-multiparty');
var express = require('express');
var router = express.Router();

var handleErrors = require('../services/error-handler').handleErrors;
var sendInfo = require('../services/error-handler').sendInfo; 
var errorCodes = require('../services/error-codes').errorCodes;
var common = require('../services/common');
var config = require('../config');
var multipartMiddleware = multipart(config.cmConfig);
var log = require('../services/log');
var Student = require('../services/mongo').Student;
var Sign = require('../services/mongo').Sign;
var SignRecord = require('../services/mongo').SignRecord;

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
  delete req.body._id;
  // 禁止修改学生手机号码
  delete req.body.phone;
  Student.findByIdAndUpdate(req.params['id'], req.body, { new: true }, function (err, newStudent) {
    if (!err) {
      console.log(newStudent);
      sendInfo(errorCodes.Success, res, newStudent);
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

router.get('/images/:id', function (req, res) {
  var id = req.params['id'];
  if (id < 1 || id > config.defaultImagesCount) {
    sendInfo(errorCodes.NoDefaultImageError, res, '');   
  } else {
    sendInfo(errorCodes.Success, res, config.userImagesUrlPrefix + id + '.png');
  }
});

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
        .project('state distance createdAt confirmAt signId')
        .group({ _id: '$signId', records: { $push: '$$ROOT' } })
        .skip(page * limit)
        .limit(limit)
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
            signNumber: signs[index].getSignIn(),
            signAt: record.createdAt,
            confirmAt: record.confirmAt
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

module.exports = router;