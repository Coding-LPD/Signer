var wrapData = require('../services/error-handler').wrapData;
var wrapError = require('../services/error-handler').wrapError; 
var errorCodes = require('../services/error-codes').errorCodes;
var common = require('../services/common');
var Teacher = require('../services/mongo').Teacher;
var Course = require('../services/mongo').Course;
var ChatRoom = require('../services/mongo').ChatRoom;
var ChatMsg = require('../services/mongo').ChatMsg;

function getRoomList(studentId, teacherId) {
  var rooms;

  return Promise.resolve()
    .then(function () {
      // 若有指定教师id，则查询教师
      if (teacherId) {
        return Course.find({ teacherId: teacherId });
      }
    })
    .then(function (findedCourses) {
      /**
       * 若有指定教师id，则查询教师相关聊天室
       * 若无，则查询学生id里面包含studentId的聊天室
       */    
      if (teacherId) {
        var courseIds = findedCourses.map(function (c) {
          return c._id; 
        });
        return ChatRoom.find({ courseId: { $in: courseIds } });
      } else {
        return ChatRoom.find({ studentIds: studentId });
      }
    })
    .then(function (findedRooms) {
      rooms = findedRooms;

      // 查询这些聊天室最后一条聊天信息
      return Promise.all(findedRooms.map(function (room) {
        return ChatMsg.findOne({ courseId: room.get('courseId') }, null, { sort: '-createdAt' });
      }));
    })
    .then(function (lastMsgs) {
      var retData = lastMsgs.map(function (msg, index) {
        return {
          courseId: rooms[index].get('courseId'),
          name: rooms[index].get('name'),
          avatar: rooms[index].get('avatar'),
          count: rooms[index].get('studentIds').length + 1,
          msg: msg
        };
      });

      return wrapData(errorCodes.Success, retData);
    })
    .catch(function (err) {
      if (err.code) {
        return wrapData(err.code, []);
      } else {
        return wrapError(err, []);
      }
    });
}

exports.getRoomList = getRoomList;