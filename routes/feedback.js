var express = require('express');
var router = express.Router();
var moment = require('moment');

var handleErrors = require('../services/error-handler').handleErrors;
var sendInfo = require('../services/error-handler').sendInfo; 
var errorCodes = require('../services/error-codes').errorCodes;
var common = require('../services/common');
var config = require('../config');
var multipartMiddleware = multipart(config.cmConfig);
var log = require('../services/log');

var Feedback = require('../services/mongo').Feedback;

router.get('/', function (req, res) {
  Feedback.find(function (err, feedbacks) {
    if (err) {
      handleErrors(err, res, []);
    } else {
      sendInfo(errorCodes.Success, res, feedbacks);
    }
  });    
});

router.post('/', function (req, res) {
  var studentId = req.body.studentId;
  var teacherId = req.body.teacherId;
  var phone = req.body.phone;
  var name = req.body.name;
  var content = req.body.content;
  var now = moment().format('YYYY-MM-DD HH:mm:ss');

  var feedback = new Feedback(req.body);
  feedback.set('createdAt', now);

  feedback.save(function (err, savedFeedback) {
    if (err) {
      handleErrors(err, res, {});
    } else {
      sendInfo(errorCodes.Success, res, savedFeedback);
    }
  })    
});

module.exports = router;