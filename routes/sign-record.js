var moment = require('moment');
var express = require('express');
var router = express.Router();

var handleErrors = require('../services/error-handler').handleErrors;
var sendInfo = require('../services/error-handler').sendInfo; 
var errorCodes = require('../services/error-codes').errorCodes;
var common = require('../services/common');
var signSocket = require('../sockets/sign');

var Student = require('../services/mongo').Student;
var SignStudent = require('../services/mongo').SignStudent;
var Sign = require('../services/mongo').Sign;
var SignRecord = require('../services/mongo').SignRecord;
var Position = require('../services/mongo').Position;

router.get('/', function (req, res) {
  SignRecord.find({}, function (err, signRecords) {
    sendInfo(errorCodes.Success, res, signRecords);
  });  
});

router.post('/', function (req, res) {
  var studentId = req.body.studentId;  
  var signId = req.body.signId;
  var studentLng = +req.body.longitude;
  var studentLat = +req.body.latitude;
  var battery = +req.body.battery;
  var type = +req.body.type;
  var phoneId = req.body.phoneId;
  var student, sign, promises = [];

  // 查询相应学生和签到信息
  promises.push(Student.findById(studentId));
  promises.push(Sign.findById(signId));
  Promise.all(promises)
    .then(function (results) {
      student = results[0];
      sign = results[1];
      var teacherId = sign.get('teacherId');
      var courseId = sign.get('courseId');
      var number = student.get('number');

      // 学生没有设置学号
      if (!number) {
        return Promise.reject({ code: errorCodes.StudentNumberEmpty });
      }

      return SignStudent.find({ teacherId: teacherId, courseId: courseId, number: number });
    })
    .then(function (signStudents) {
      // 学生不属于该课程
      if (signStudents.length <= 0) {
        return Promise.reject({ code: errorCodes.NotInCourse });
      }

      // 不在签到时间范围内
      var now = moment();
      var startTime = moment(sign.get('startTime'));
      var endTime = moment(sign.get('endTime'));        
      if (now.isBefore(startTime)) {
        return Promise.reject({ code: errorCodes.SignNotStart });
      } else if (now.isAfter(endTime)) {
        return Promise.reject({ code: errorCodes.SignHasEnd });
      }

    //   promises = [];
    //   promises.push(SignRecord.findOne({ signId: signId, studentId: studentId, type: type}));
    //   promises.push(SignRecord.findOne({ signId: signId, phoneId: phoneId, type: type }));
    //   return Promise.all(promises);
    // })
    // .then(function (results) {
    //   // 一个学生只有一次机会参与某次签到（课前一次，课后一次）
    //   if (results[0]) {
    //     return Promise.reject({ code: errorCodes.StudentHasSign });
    //   }
    //   // 一部手机只有一次机会参与某次签到（课前一次，课后一次）
    //   if (results[1]) {
    //     return Promise.reject({ code: errorCodes.PhoneHasSign });
    //   }

      return Position.find({ signId: signId });
    })
    .then(function (teacherPositions) {
      // 教师没有定位成功    
      if (teacherPositions.length <= 0) {
        return Promise.reject({ code: errorCodes.TeacherNotLocate });   
      }     

      // 计算距离，保存该签到信息
      var teacherPos = teacherPositions[0];    
      var distance = common.getFlatternDistance(teacherPos.latitude, teacherPos.longitude, studentLat, studentLng);
      var signRecord = new SignRecord(req.body);
      signRecord.set('batter', battery);
      signRecord.set('type', type);
      signRecord.set('courseId', sign.get('courseId'));
      signRecord.set('state', 0);
      signRecord.set('distance', Math.round(distance));
      signRecord.set('studentName', student.get('name'));
      signRecord.set('studentAvatar', student.get('avatar'));
      signRecord.set('createdAt', moment().format('YYYY-MM-DD HH:mm:ss'));

      return signRecord.save();
    })
    .then(function (savedData) {
      // 响应http
      sendInfo(errorCodes.Success, res, savedData);
      // 推送数据给浏览器
      signSocket.send(signSocket.events.sign, savedData);
    })
    .catch(function (err) {
      if (err.code) {
        sendInfo(err.code, res, {});
      } else {
        handleErrors(err, res, {});
      }
    });
});

router.post('/:id/assent', function (req, res) {   
  confirm(req, res, 1);
});

router.post('/:id/refusal', function (req, res) {
  confirm(req, res, 2);
});

router.post('/assent/all', function (req, res) {
  confirmAll(req, res, 1);
});

router.post('/refusal/all', function (req, res) {
  confirmAll(req, res, 2);
});

router.post('/search', function (req, res) {
  // 必须要有查询条件
  if (common.isEmptyObject(req.body)) {
    sendInfo(errorCodes.SearchEmpty, res);
    return;
  }
  SignRecord.find(req.body, function (err, signRecors) {
    if (!err) {
      sendInfo(errorCodes.Success, res, signRecors);
    } else {
      handleErrors(err, res, []);
    }
  });
});

function confirm(req, res, state) {
  var type, record;
  var now = moment().format('YYYY-MM-DD HH:mm:ss');

  // 修改签到状态
  SignRecord.findByIdAndUpdate(req.params['id'], { state: state, confirmAt: now }, { new: true })
    .then(function (savedRecord) {
      record = savedRecord;
      type = savedRecord.get('type');      
      var signIn = type == 0 ? { beforeSignIn: 1 } : { afterSignIn: 1 };

      // 签到完成人数加1
      return Sign.findByIdAndUpdate(savedRecord.get('signId'), { $inc: signIn }, { new: true })
    })
    .then(function (updatedSign) {
      // 响应http
      sendInfo(errorCodes.Success, res, { signIn: updatedSign.getSignIn(type) });
      
      // 推送数据给手机客户端
      signSocket.noticeStudent(record.get('studentId'), '');
    })
    .catch(function (err) {
      if (err.code) {
        sendInfo(err.code, res, {});
      } else {
        handleErrors(err, res, {});
      }
    });
}

function confirmAll(req, res, state) {
  var ids = req.body.recordIds;
  var type, count, records;
  var now = moment().format('YYYY-MM-DD HH:mm:ss');

  // 修改签到状态
  Promise.all(ids.map(function (id) {
    return SignRecord.findByIdAndUpdate(id, { state: state, confirmAt: now }, { new: true });
  }))
  .then(function (savedRecords) {
    records = savedRecords;
    type = savedRecords[0].get('type');
    count = savedRecords.length;    
    var signIn = type == 0 ? { beforeSignIn: count } : { afterSignIn: count };

    // 签到完成人数加n
    return Sign.findByIdAndUpdate(savedRecords[0].get('signId'), { $inc: signIn }, { new: true }) 
  })
  .then(function (savedSign) {
    // 响应http
    sendInfo(errorCodes.Success, res, { signIn: savedSign.getSignIn(type) });
    
    // 推送数据给手机客户端
    records.forEach(function (record) {
      signSocket.noticeStudent(record.get('studentId'), '');
    });
  })
  .catch(function (err) {
    if (err.code) {
      sendInfo(err.code, res, {});
    } else {
      handleErrors(err, res, {});
    }
  });
}

module.exports = router;