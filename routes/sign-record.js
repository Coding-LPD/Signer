var express = require('express');
var router = express.Router();

var handleErrors = require('../services/error-handler').handleErrors;
var sendInfo = require('../services/error-handler').sendInfo; 
var errorCodes = require('../services/error-codes').errorCodes;
var common = require('../services/common');
var SignRecord = require('../services/mongo').SignRecord;

router.get('/', function (req, res) {
  SignRecord.find({}, function (err, signRecords) {
    sendInfo(errorCodes.Success, res, signRecords);
  });  
});

module.exports = router;