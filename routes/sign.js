var moment = require('moment');
var express = require('express');
var router = express.Router();

var handleErrors = require('../services/error-handler').handleErrors;
var sendInfo = require('../services/error-handler').sendInfo; 
var errorCodes = require('../services/error-codes').errorCodes;
var common = require('../services/common');
var Course = require('../services/mongo').Course;
var Sign = require('../services/mongo').Sign;

router.get('/', function (req, res) {
  Sign.find(function (err, signs) {
    if (!err) {
      sendInfo(errorCodes.Success, res, signs);
    } else {
      handleErrors(err, res, []);
    }
  });
});

router.post('/', function (req, res) {
  var courseId = req.body.courseId;
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

  Course.find({_id: courseId}, function (err, courses) {
    if (err) {
      handleErrors(err, res, {});
      return;      
    }
    var course = courses[0];
    var state = 0, now = moment(new Date());
    if (now.isBefore(startTime)) {
      state = 0;
    } else if (now.isAfter(endTime)) {
      state = 2;
    } else {
      state = 1;
    }
    var createdAt = now.format('YYYY-MM-DD HH:mm');
    var code = Sign.generateSignCode();
    
    var newSign = new Sign(req.body);    
    newSign.set('courseName', course.get('name'));
    newSign.set('teacherId', course.get('teacherId'));
    newSign.set('createdAt', createdAt);
    newSign.set('state', state);
    newSign.set('code', code);

    newSign.save(function (err, savedSign) {
      if (!err) {
        sendInfo(errorCodes.Success, res, savedSign);
      } else {
        handleErrors(err, res, {});
      }
    });
  });
});

router.delete('/:id', function (req, res) {
  Sign.findByIdAndRemove(req.params['id'], function (err, deletedSign) {
    if (!err) {
      sendInfo(errorCodes.Success, res, deletedSign);
    } else {
      handleErrors(err, res, {});
    }
  });
});

router.post('/search', function (req, res) {
  // 必须要有查询条件
  if (common.isEmptyObject(req.body)) {
    sendInfo(errorCodes.SearchEmpty, res, []);
    return;
  }
  Sign.find(req.body, function (err, signs) {
    if (!err) {
      sendInfo(errorCodes.Success, res, signs);
    } else {
      handleErrors(err, res, []);
    }
  });
});

module.exports = router;