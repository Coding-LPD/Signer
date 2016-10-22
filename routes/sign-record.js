var express = require('express');
var router = express.Router();

var handleErrors = require('../services/error-handler').handleErrors;
var sendInfo = require('../services/error-handler').sendInfo; 
var errorCodes = require('../services/error-codes').errorCodes;
var common = require('../services/common');
var Student = require('../services/mongo').Student;
var SignRecord = require('../services/mongo').SignRecord;

router.get('/', function (req, res) {
  SignRecord.find({}, function (err, signRecords) {
    sendInfo(errorCodes.Success, res, signRecords);
  });  
});

router.post('/', function (req, res) {
  var signRecord = new SignRecord(req.body);
  signRecord.set('state', 0);
  signRecord.set('distance', 100);  //测试
  Student.findById(signRecord.get('studentId'), function (err, student) {
    if (err) {
      handleErrors(err, res, {});
      return;
    }

    signRecord.set('studentName', student.get('name'));
    signRecord.set('studentAvatar', student.get('avatar'));
    signRecord.save(function () {
      if (err) {
        handleErrors(err, res, {});        
      } else {
        sendInfo(errorCodes.Success, res, signRecord);
      }
    });    
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