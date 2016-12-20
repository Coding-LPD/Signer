var moment = require('moment');
var express = require('express');
var router = express.Router();

var handleErrors = require('../services/error-handler').handleErrors;
var sendInfo = require('../services/error-handler').sendInfo; 
var errorCodes = require('../services/error-codes').errorCodes;
var common = require('../services/common');
var Course = require('../services/mongo').Course;
var Teacher = require('../services/mongo').Teacher;
var Sign = require('../services/mongo').Sign;
var SignRecord = require('../services/mongo').SignRecord;
var SignStudent = require('../services/mongo').SignStudent;
var Position = require('../services/mongo').Position;
var ChatRoom = require('../services/mongo').ChatRoom;
var ChatMsg = require('../services/mongo').ChatMsg;

router.get('/', function (req, res) {
  Course.find(function (err, courses) {
    if (!err) {
      sendInfo(errorCodes.Success, res, courses);
    } else {
      handleErrors(err, res, []);
    }
  });
});

router.post('/', function (req, res) {
  var newCourse = new Course(req.body);
  newCourse.set('createdAt', moment().format('YYYY-MM-DD HH:mm:ss'))
  newCourse.save(function (err, savedCourse) {
    if (!err) {
      sendInfo(errorCodes.Success, res, savedCourse);
    } else {
      handleErrors(err, res, {});
    }
  });
});

router.put('/:id', function (req, res) {
  var courseId = req.params['id'];
  var course;

  delete req.body._id;
  delete req.body.teacherId;
  delete req.body.studentCount;
  delete req.body.signCount;

  Course.findByIdAndUpdate(courseId, req.body, { new: true }, function (err, savedCourse) {
    if (err) {
      handleErrors(err, res, {});
    } else {
      sendInfo(errorCodes.Success, res, savedCourse);
    }
  });    
});

router.delete('/:id', function (req, res) {
  var courseId = req.params['id'];
  var promises = [];
  var course;  

  // 删除该课程
  Course.findByIdAndRemove(courseId)
    .then(function (deletedCourse) {
      course = deletedCourse;

      // 查询课程相关签到
      promises.push(Sign.find({ courseId: courseId }));
      // 删除课程导入的学生、签到记录，更新使用该课程导入的学生的签到的信息      
      promises.push(SignStudent.remove({ courseId: courseId }));
      promises.push(SignRecord.remove({ courseId: courseId }));
      promises.push(Sign.update({ relatedId: courseId }, { relatedId: '', studentCount: 0 }, { multi: true }));
      // 删除课程相关的聊天室与聊天信息
      promises.push(ChatRoom.remove({ courseId: courseId }));
      promises.push(ChatMsg.remove({ courseId: courseId }));
      
      return Promise.all(promises);
    })
    .then(function (results) {
      var signs = results[0];
      promises = [];
      
      // 删除相关签到和其定位信息
      promises.push(Sign.remove({ courseId: courseId }));
      promises = promises.concat(signs.map(function (sign) {
        return Position.remove({ signId: sign._id });
      }));

      return Promise.all(promises);
    })
    .then(function () {
      sendInfo(errorCodes.Success, res, course);
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
  var sortby = req.query['sortby'] || '';
  var order = req.query['order'] || 1;
  var options = null;
  if (sortby) {
    var sort = (order == 1 ? '' : '-') + sortby;
    options = { sort: sort };
  }
  // 必须要有查询条件
  if (common.isEmptyObject(req.body)) {
    sendInfo(errorCodes.SearchEmpty, res);
    return;
  }
  Course.find(req.body, null, options, function (err, courses) {
    if (!err) {
      sendInfo(errorCodes.Success, res, courses);
    } else {
      handleErrors(err, res, []);
    }
  });
});

// 课程最近一次签到
router.get('/:id/latestSignRecords', function (req, res) {
  var maxAvatarNum = 10;
  var course, teacher, signNum, promises;

  Course.findById(req.params['id'], 'name time location teacherId')
    .then(function (finedCourse) {
      // 课程不存在
      if (!finedCourse) {
        return Promise.reject({ code: errorCodes.CourseNotExist });
      }

      course = finedCourse;
      promises = [];
      // 查询相关教师
      promises.push(Teacher.findById(course.get('teacherId')));
      // 查询签到次数
      promises.push(Sign.count({ courseId: course._id }));      
      // 查找该课程最近一次签到
      promises.push(Sign.findOne({ courseId: course._id }, null, { sort: '-createdAt' }));
            
      return Promise.all(promises);
    })
    .then(function (results) {
      teacher = results[0];
      signNum = results[1];
      var sign =  results[2];

      // 该课程没有相关教师
      if (!teacher) {
        return Promise.reject({ code: errorCodes.NoRelatedTeacher });
      }      
      // 该课程还没有进行过签到
      if (!sign) {
        return Promise.reject({ code: errorCodes.NoRelatedSign });
      }

      // 查找该签到最近的几十条签到记录
      return SignRecord.aggregate()
        .match({ signId: '' + sign._id })
        .sort('-createdAt')
        .project('studentAvatar studentName')
        .limit(maxAvatarNum)
        .exec()
    })
    .then(function (records) {
      // 修改返回的记录
      records = records.map(function (record) {
        return {
          _id: record._id,
          name: record.studentName,
          avatar: record.studentAvatar
        };
      });
      // 修改返回的课程数据
      course = course.toObject();
      course.teacherName = teacher.get('name');
      delete course.teacherId;
      // 返回数据
      var retData = {
        signNum: signNum,
        course: course,
        records: records
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

// 该课程中，指定学生的签到情况
router.get('/:id/students/:studentId/signRecords', function (req, res) {
  var signs;

  // 查询签到信息
  Sign.find({ courseId: req.params['id'] })
    .then(function (findedSigns) {
      signs = findedSigns;
      // 查询签到中该学生对应的签到记录
      return Promise.all(findedSigns.map(function (sign) {
        return SignRecord.find({ signId: sign._id, studentId: req.params['studentId'] });          
      }));
    })
    .then(function (results) {
      var retData = [];
      results.forEach(function (records, index) {
        // 对应签到没有相应的学生签到记录，false
        if (records.length <= 0) {
          retData.push({
            signId: signs[index]._id,
            time: moment(signs[index].startTime).format('YYYY-MM-DD'),
            tag: false
          });
        }
        // 有签到记录，若教师没批准，则表示未签到false，若批注了，则表示已签到true
        records.forEach(function (record) {
          retData.push({
            signId: signs[index]._id,
            time: moment(signs[index].startTime).format('YYYY-MM-DD'),
            tag: record.get('state') > 0
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

router.get('/:id/statistics/latest', function (req, res) {
  var courseId = req.params['id'];
  var sign, batteryCostData, top10BatteryCost, last10BatteryCost;

  // 查找课程最近一次签到
  Sign.findOne({ courseId: courseId }, null, { sort: '-createdAt' })
    .then(function (findedSign) {
      if (!findedSign) {
        return Promise.reject({ code: errorCodes.NoRelatedSign });
      }

      sign = findedSign;

      // 根据签到和课程，查询学生课前课后的电量，并进行分组
      return SignRecord.aggregate()
        .match({ signId: '' + sign._id, courseId: courseId })
        .sort('type')
        .project('signId studentId studentName battery')        
        .group({ _id: { signId: '$signId', studentId: '$studentId', studentName: '$studentName' }, battery: { $push: '$battery' } })
        .exec();
    })
    .then(function (results) {
      // 提取学生id和对应的电量消耗
      var batterys = [];
      results.forEach(function (r) {
        if (r.battery.length == 2) {
          batterys.push({
            studentId: r._id.studentId,
            name: r._id.studentName,
            batteryCost: r.battery[0] - r.battery[1]
          });
        }
      });
      // 电量按照一定百分比分组，并统计每组人数
      batteryCostData = batteryCostGroup(batterys.map(function (b) {
        return b.batteryCost;
      }));
      // 按照电量消耗从小到大排序
      batterys = batterys.sort(function (a, b) {
        return a.batteryCost - b.batteryCost;
      });
      // 电量消耗前十
      top10BatteryCost = batterys.slice(0, 10);
      // 电量消耗后十
      last10BatteryCost = batterys.slice(-11).reverse();

      sendInfo(errorCodes.Success, res, {
        studentCount: sign.get('studentCount'),
        beforeSignIn: sign.get('beforeSignIn'),
        afterSignIn: sign.get('afterSignIn'),  
        batteryCost: batteryCostData,
        top10BatteryCost: top10BatteryCost,
        last10BatteryCost: last10BatteryCost      
      });
    })
    .catch(function (err) {
      if (err.code) {
        sendInfo(err.code, res, {});
      } else {
        handleErrors(err, res, {});
      }
    });
});

// 统计最近几天内的签到比例，同一天多个签到，则统计平均值
router.get('/:id/statistics/all', function (req, res) {
  var courseId = req.params['id'];
  var maxDayCount = 8;  // 最近8天内的签到比例

  // 查新该课程最近几天的签到
  Sign.find({ courseId: courseId }, null, { sort: '-startTime' })
    .then(function (signs) {
      if (signs.length <= 0) {
        return Promise.reject({ code: errorCodes.NoRelatedSign });
      }      

      var startTime = moment(signs[0].get('startTime'));      
      var studentCount = 0, signInCount = 0;
      var preTime = startTime;
      var signIn = [];

      // 循环中不会把最后一项加入结果中，所以添加一个冗余项，保证原本数据都能加入结果中
      signs.push(new Sign({ startTime: '2000-01-01', studentCount: 0, beforeSignIn: 0 }));

      // 计算同一天签到的平均比例，统计出最近几天的签到即可
      for (var i=0; i<signs.length; i++) {
        var sign = signs[i];
        startTime = moment(sign.get('startTime')); 

        /**
         * 同一天的签到，则累加学生数量和签到数量
         * 不同天的签到，则把之前累加好的保存，再初始化来累加
         * 已经超过指定天数，把最后一天的剔除
         */
        if (startTime.isSame(preTime, 'day')) {
          studentCount += sign.get('studentCount');
          signInCount += sign.get('beforeSignIn');
        } else if (signIn.length <= maxDayCount) {
          signIn.push({ time: preTime.format('YYYY-MM-DD'), ratio: Math.round(signInCount / studentCount * 100) });                    
          studentCount = sign.get('studentCount');
          signInCount = sign.get('beforeSignIn');
          preTime = startTime;
        } else {
          signIn = signIn.slice(-1);
          break;
        }
      }

      var retData = {
        signIn: signIn.reverse()  // 使数据按时间顺序排列
      }
      sendInfo(errorCodes.Success, res, retData);
    })
    .catch(function (err) {
      if (err.code) {
        sendInfo(err.code, res, {});
      } else {
        handleErrors(err, res, {});
      }
    });
})

function batteryCostGroup(batteryCost) {
  // 0: -100~0%
  // 1: 0~30%
  // 2: 30~50%
  // 3: 50~70%
  // 4: 70~90%
  // 5: 90~100%
  var results = [0, 0, 0, 0, 0, 0];

  batteryCost.forEach(function (cost) {
    if (cost < 0) {
      results[0]++;
    } else if (cost < 30) {
      results[1]++;
    } else if (cost < 50) {
      results[2]++;
    } else if (cost < 70) {
      results[3]++;
    } else if (cost < 90) {
      results[4]++;
    } else {
      results[5]++;
    }
  });

  return results;
}

module.exports = router;