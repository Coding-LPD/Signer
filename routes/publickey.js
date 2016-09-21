var express = require('express');
var router = express.Router();
var fs = require('fs');
var path = require('path');
var sendInfo = require('../services/error-handler').sendInfo;
var handleErrors = require('../services/error-handler').handleErrors;
var errorCodes = require('../services/error-codes').errorCodes;

// 获取公钥
router.get('/', function (req, res) {
  var publickeyPath = path.resolve(__dirname, '../services/publickey.pem');
  fs.readFile(publickeyPath, function (err, publickey) {
    if (err) {
      handleErrors(err, res, '');
    } else {
      sendInfo(errorCodes.Success, res, publickey.toString());
    }
  });
});

module.exports = router;