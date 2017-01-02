var SignSchedule = require('./sign');
var TempFileSchedule = require('./tempFile');

function start() {
  console.log('schedule start');
  SignSchedule.start();
  TempFileSchedule.start();
}

exports.start = start;