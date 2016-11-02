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
  Course.findByIdAndRemove(req.params['id'], function (err, deletedCourse) {
    if (!err) {
      sendInfo(errorCodes.Success, res, deletedCourse);
    } else {
      handleErrors(err, res, {});
    }
  })
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

  Sign.find({ courseId: req.params['id'] })
    .then(function (findedSigns) {
      signs = findedSigns;
      return Promise.all(findedSigns.map(function (sign) {
        return SignRecord.find({ signId: sign._id, studentId: req.params['studentId'] });          
      }));
    })
    .then(function (results) {
      var retData = [];
      results.forEach(function (records, index) {
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