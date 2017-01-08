var express = require('express');
var moment = require('moment');
var router = express.Router();

var handleErrors = require('../services/error-handler').handleErrors;
var sendInfo = require('../services/error-handler').sendInfo; 
var errorCodes = require('../services/error-codes').errorCodes;
var common = require('../services/common');
var Teacher = require('../services/mongo').Teacher;
var Sign = require('../services/mongo').Sign;
var ChatRoom = require('../services/mongo').ChatRoom;
var ChatMsg = require('../services/mongo').ChatMsg;

router.get('/', function (req, res) {
  Teacher.find(function (err, teachers) {
    if (!err) {
      sendInfo(errorCodes.Success, res, teachers);
    } else {
      handleErrors(err, res, []);
    }
  });
});

router.put('/:id', function (req, res) {
  var teacherId = req.params['id'];
  var teacher;

  // 禁止修改教师id和手机号
  delete req.body._id;
  delete req.body.phone;

  Teacher.findByIdAndUpdate(teacherId, req.body, { new: true })
    .then(function (savedTeacher) {
      teacher = savedTeacher;

      // 修改相关发言的头像、昵称
      var obj = {};
      if (req.body.name) {
        obj.name = req.body.name;
      }
      if (req.body.avatar) {
        obj.avatar = req.body.avatar;
      }
      return ChatMsg.update({ teacherId: teacherId }, obj, { multi: true });
    })
    .then(function () {
      sendInfo(errorCodes.Success, res, teacher);
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
    sendInfo(errorCodes.SearchEmpty, res, []);
    return;
  }
  Teacher.find(req.body, function (err, teachers) {
    if (!err) {
      sendInfo(errorCodes.Success, res, teachers);
    } else {
      handleErrors(err, res, []);
    }
  });
});

router.post('/:id/signsInPeriod', function (req, res) {
  var teacherId = req.params['id'];
  var format = 'YYYY-MM-DD HH:mm:ss';
  var startTime, endTime;

  Promise.resolve()
    .then(function () {      
      startTime = moment(req.body.startTime).format(format);
      endTime = moment(req.body.endTime).add(1, 'day').format(format);

      // 查询在这段时间内的签到数据，包含开始时间和结束时间
      return Sign.find({ teacherId: teacherId, startTime: { $gt: startTime, $lt: endTime } }, 'color courseName startTime', { sort: 'startTime' });
    })
    .then(function (signs) {      
      var date;
      var lastDate = moment(startTime);
      var index = 0;
      var calendar = [];
      signs.forEach(function (sign) {
        date = moment(sign.get('startTime'));
        while (!date.isSame(lastDate, 'days')) {
          index++;
          lastDate.add(1, 'day');
        }
        if (!calendar[index]) {
          calendar[index] = [];
        }
        calendar[index].push(sign.toObject());
      });
      calendar.length += moment(endTime).diff(lastDate, 'days') - 1;
      sendInfo(errorCodes.Success, res, calendar);
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