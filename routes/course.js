var moment = require('moment');
var express = require('express');
var router = express.Router();

var handleErrors = require('../services/error-handler').handleErrors;
var sendInfo = require('../services/error-handler').sendInfo; 
var errorCodes = require('../services/error-codes').errorCodes;
var common = require('../services/common');
var Course = require('../services/mongo').Course;

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
  var startTime = new Date(req.body.startTime);
  var endTime = new Date(req.body.endTime);

  // 时间格式不对，返回错误信息 
  if (isNaN(startTime.valueOf())) {
    sendInfo(errorCodes.CourseStartTimeInvalid, res, {});
    return;
  }
  if (isNaN(endTime.valueOf())) {
    sendInfo(errorCodes.CourseEndTimeInvalid, res, {});
    return;
  }
  // 起始时间晚于结束时间
  if (moment(endTime).isBefore(startTime)) {
    sendInfo(errorCodes.CourseTimeRangeError, res, {});
    return;
  }

  var newCourse = new Course(req.body);
  // 根据当前时间与课程时间，判断课程状态
  var now = moment(new Date());
  if (now.isBefore(startTime)) {
    newCourse.set('state', 0);
  } else if (now.isAfter(endTime)) {
    newCourse.set('state', 2);
  } else {
    newCourse.set('state', 1);
  }
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

module.exports = router;