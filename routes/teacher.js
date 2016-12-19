var express = require('express');
var router = express.Router();

var handleErrors = require('../services/error-handler').handleErrors;
var sendInfo = require('../services/error-handler').sendInfo; 
var errorCodes = require('../services/error-codes').errorCodes;
var common = require('../services/common');
var Teacher = require('../services/mongo').Teacher;
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

module.exports = router;