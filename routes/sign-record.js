var moment = require('moment');
var express = require('express');
var router = express.Router();

var handleErrors = require('../services/error-handler').handleErrors;
var sendInfo = require('../services/error-handler').sendInfo; 
var errorCodes = require('../services/error-codes').errorCodes;
var common = require('../services/common');
var Student = require('../services/mongo').Student;
var Sign = require('../services/mongo').Sign;
var SignRecord = require('../services/mongo').SignRecord;
var Position = require('../services/mongo').Position;

router.get('/', function (req, res) {
  SignRecord.find({}, function (err, signRecords) {
    sendInfo(errorCodes.Success, res, signRecords);
  });  
});

router.post('/', function (req, res) {
  var signRecord = new SignRecord(req.body);
  var signId = req.body.signId;
  var studentLng = +req.body.longitude;
  var studentLat = +req.body.latitude;
  var battery = +req.body.battery;
  var type = +req.body.type;

  var promises = [];
  promises.push(Position.find({ signId: signId }));
  promises.push(Sign.findById(signId));
  promises.push(Student.findById(signRecord.get('studentId')));

  Promise.all(promises).then(function (results) {
    if (results[0].length <= 0) {
      return Promise.reject({ code: errorCodes.TeacherNotLocate });
    }

    var teacherPos = results[0][0];
    var sign = results[1];
    var student = results[2];

    var distance = common.getFlatternDistance(teacherPos.latitude, teacherPos.longitude, studentLat, studentLng);
    signRecord.set('batter', battery);
    signRecord.set('type', type);
    signRecord.set('courseId', sign.get('courseId'));
    signRecord.set('state', 0);
    signRecord.set('distance', Math.round(distance));
    signRecord.set('studentName', student.get('name'));
    signRecord.set('studentAvatar', student.get('avatar'));
    signRecord.set('createdAt', moment(new Date()).format('YYYY-MM-DD HH:mm:ss'));
    return signRecord.save();
  })
  .then(function (savedData) {
    sendInfo(errorCodes.Success, res, savedData);
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

module.exports = router;