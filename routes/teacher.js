var express = require('express');
var router = express.Router();

var handleErrors = require('../services/error-handler').handleErrors;
var sendInfo = require('../services/error-handler').sendInfo; 
var errorCodes = require('../services/error-codes').errorCodes;
var common = require('../services/common');
var Teacher = require('../services/mongo').Teacher;

router.get('/', function (req, res) {
  Teacher.find(function (err, teachers) {
    if (!err) {
      sendInfo(errorCodes.Success, res, teachers);
    } else {
      handleErrors(err, res, []);
    }
  });
});

router.post('/search', function (req, res) {
  // 必须要有查询条件
  if (common.isEmptyObject(req.body)) {
    sendInfo(errorCodes.SearchEmpty, res, []);
    return;
  }
  Teacher.find(req.body, function (err, teachers) {
    if (!err) {
      sendInfo(errorCodes.Success, res, teachers);
    } else {
      handleErrors(err, res, []);
    }
  });
});

module.exports = router;