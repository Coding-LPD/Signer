var nsp;
var namespace = '/sign';
var events = {
  studentIn: 'student-in',
  studentOut: 'student-out',
  sign: 'sign',
  notice: 'notice'
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
  client.on(events.studentIn, studentIn(client));
  client.on(events.studentOut, studentOut(client));
}

function disconnect() {
  console.log('client disconnect');
}

function handleError(err) {
  console.log('socket error: ' + err);
}

function studentIn(client) {
  return function (data) {    
    students[data] = client;
  }
}

function studentOut(client) {
  return function (data) {
    students[data] = client;
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