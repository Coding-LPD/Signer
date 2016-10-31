var moment = require('moment');
var express = require('express');
var router = express.Router();

var handleErrors = require('../services/error-handler').handleErrors;
var sendInfo = require('../services/error-handler').sendInfo; 
var errorCodes = require('../services/error-codes').errorCodes;
var common = require('../services/common');
var Course = require('../services/mongo').Course;
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
  var course;

  Course.findById(req.params['id'])
    .then(function (finedCourse) {
      // 课程不存在
      if (!finedCourse) {
        return Promise.reject(errorCodes.CourseNotExist);
      }

      course = finedCourse;
      // 查找该课程最近一次签到
      return Sign.findOne({ courseId: course._id }, null, { sort: '-createdAt' })
    })
    .then(function (sign) {
      // 该课程还没有进行过签到
      if (!sign) {
        return Promise.reject(errorCodes.NoRelatedSign);
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
      records = records.map(function (record) {
        return {
          _id: record._id,
          name: record.studentName,
          avatar: record.studentAvatar
        };
      });
      var retData = {
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

module.exports = router;