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
  newCourse.save(function (err, savedCourse) {
    if (!err) {
      sendInfo(errorCodes.Success, res, savedCourse);
    } else {
      handleErrors(err, res, {});
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
  // 必须要有查询条件
  if (common.isEmptyObject(req.body)) {
    sendInfo(errorCodes.SearchEmpty, res);
    return;
  }
  Course.find(req.body, function (err, courses) {
    if (!err) {
      sendInfo(errorCodes.Success, res, courses);
    } else {
      handleErrors(err, res, []);
    }
  });
});

router.get('/:id/latestSignRecords', function (req, res) {
  var maxAvatarNum = 20;
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

module.exports = router;