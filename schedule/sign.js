var schedule = require('node-schedule');
var moment = require('moment');

var Sign = require('../services/mongo').Sign;

function start() {
  // 每10分钟更新签到的状态
  schedule.scheduleJob('0 */10 * * * *', function () {
    updateState();
  });
}

// 更新签到的状态
function updateState() {
  console.log('update sign state...', moment().format('YYYY-MM-DD HH:mm:ss'));
  
  Sign.find({ state: { $lt: 2 } })
    .then(function (signs) {
      var startTime, endTime;
      var now = moment();

      return Promise.all(signs.map(function (sign) {
        startTime = moment(sign.get('startTime'));
        endTime = moment(sign.get('endTime'));
        /**
         * 签到开始，修改状态为1
         * 签到结束，修改状态为2
         */        
        if (now.isAfter(startTime) && now.isBefore(endTime)) {
          sign.set('state', 1);       
        } else if (now.isAfter(endTime)) {
          sign.set('state', 2);
        }
        return sign.save();
      }));
    })
    .then(function () {
      console.log('update sign state success');
    })
    .catch(function (err) {
      console.log('update sign state error: ', err);
    });
}

exports.start = start;