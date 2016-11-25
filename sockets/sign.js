var ChatRoom = require('../routes/chat-room');
var ChatMsg = require('../routes/chat-msg');

var nsp;
var namespace = '/sign';
var events = {
  // sign
  studentIn: 'student-in',
  studentOut: 'student-out',
  sign: 'sign',
  notice: 'notice',

  // chatroom
  roomList: 'room-list',
  msgList: 'msg-list',
  newMsg: 'new-msg'
};
var students = {};

function setSocket(io) {
  
  nsp = io.of(namespace);

  nsp.on('connect', function (client) {
    console.log('client connect');
    listen(client);
  });

}

function listen(client) {
  client.on('disconnect', disconnect);
  client.on('error', handleError);

  // sign
  client.on(events.studentIn, onStudentIn(client));
  client.on(events.studentOut, onStudentOut(client));

  // chatroom
  client.on(events.roomList, onRoomList(client));
  client.on(events.msgList, onMsgList(client));
  client.on(events.newMsg, onNewMsg(client));
}

function disconnect() {
  console.log('client disconnect');
}

function handleError(err) {
  console.log('socket error: ' + err);
}

function onStudentIn(client) {
  return function (data) {
    students[data] = client;
  }
}

function onStudentOut(client) {
  return function (data) {
    students[data] = client;
  }
}

// 监听room-list事件，返回聊天室列表，并把该客户端加入对应聊天室
function onRoomList(client) {
  return function (studentId, teacherId) {
    console.log('room-list');
    ChatRoom.getRoomList(studentId, teacherId)
      .then(function (result) {
        // 将聊天室列表发送给客户端
        client.emit(events.roomList, result);
        // 将客户端加入对应课程的聊天室
        result.data.forEach(function (d) {
          client.join(d.courseId);
        });
      });
  }
}

// 监听msg-list事件，返回指定聊天室的聊天信息
function onMsgList(client) {
  return function (courseId, page, limit) {
    console.log('msg-list');    
    ChatMsg.getMsgList(courseId, page, limit)
      .then(function (results) {
        client.emit(events.msgList, results);        
      });
  }
}

// 监听new-msg事件，返回保存好的聊天信息，通知同一个聊天室的人有新消息
function onNewMsg(client) {  
    return function (courseId, studentId, content, teacherId) {
      console.log('new-msg');
      ChatMsg.saveMsg(courseId, studentId, content, teacherId)
        .then(function (results) {
          client.to(courseId).emit(events.newMsg, results);
        });
    }
}

function send(event, data) {
  nsp.emit(event, data);
}

function noticeStudent(id, data) {
  id = id || '';
  if (students[id]) {
    students[id].emit(events.notice, data);    
  } else {
    send(events.notice, data);
  }
}

exports.setSocket = setSocket;
exports.events = events;
exports.send = send;
exports.noticeStudent = noticeStudent;