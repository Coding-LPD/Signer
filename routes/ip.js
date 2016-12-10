var express = require('express');
var router = express.Router();

var common = require('../services/common');
var sendInfo = require('../services/error-handler').sendInfo;
var errorCodes = require('../services/error-codes').errorCodes;

router.use('/', function (req, res) {
  var ip = common.getClientIp(req);
  sendInfo(errorCodes.Success, res, ip);
});

module.exports = router;