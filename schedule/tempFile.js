var fsp = require('fs-promise');
var path = require('path');
var moment = require('moment');
var schedule = require('node-schedule');

function start() {
  // 每天0点清空临时下载文件
  schedule.scheduleJob('0 0 0 * * *', function () {
    emptyDir();
  });
}

function emptyDir() {
  console.log('empty download dir...', moment().format('YYYY-MM-DD HH:mm:ss'));

  var dirPath = path.resolve(__dirname, '../public/temp');
  fsp.emptyDir(dirPath)
    .then(function () {
      console.log('empty download dir success');
    })
    .catch(function (err) {
      console.log('empty download dir error: ', err);
    });
}

exports.start = start;