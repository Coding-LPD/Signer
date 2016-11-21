var moment = require('moment');

var wrapData = require('../services/error-handler').wrapData;
var wrapError = require('../services/error-handler').wrapError; 
var errorCodes = require('../services/error-codes').errorCodes;
var common = require('../services/common');
var Student = require('../services/mongo').Student;
var Teacher = require('../services/mongo').Teacher;
var ChatRoom = require('../services/mongo').ChatRoom;
var ChatMsg = require('../services/mongo').ChatMsg;

function getMsgList(courseId, page, limit) {
  courseId = courseId;
  page = page || 0;
  limit = limit || 18;

  return ChatMsg.find({ courseId: courseId }, null, { sort: '-createdAt', limit: 18, skip: page*limit })
    .then(function (findedMsg) {
      return wrapData(errorCodes.Success, findedMsg);
    })
    .catch(function (err) {
      if (err.code) {
        return wrapData(err.code, []);
      } else {
        return wrapError(err, []);
      }
    });
}

function saveMsg(courseId, studentId, content) {
  courseId = courseId;
  studentId = studentId || '';
  var teacherId = '';
  content = content;
  
  return Promise.resolve()
    .then(function () {
      if (studentId) {
        return Student.findById(studentId);
      } else if (teacherId) {
        return Teacher.findById(teacherId);
      } else {
        return Promise.reject({ code: errorCodes.MsgSenderIsEmpty })
      }
    })
    .then(function (sender) {
      var newMsg = new ChatMsg({
        courseId: courseId,
        studentId: studentId,
        teacherId: teacherId,
        content: content,
        avatar: sender.get('avatar'),
        name: sender.get('name'),
        createdAt: moment().format('YYYY-MM-DD HH:mm:ss')
      });

      return newMsg.save();
    })
    .then(function (savedMsg) {
      return wrapData(errorCodes.Success, savedMsg);
    })
    .catch(function (err) {
      if (err.code) {
        return wrapData(err.code, {});
      } else {
        return wrapError(err, {});
      }
    });
}

exports.getMsgList = getMsgList;
exports.saveMsg = saveMsg;