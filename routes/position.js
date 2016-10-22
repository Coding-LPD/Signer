var https = require('https');
var qs = require('querystring');
var express = require('express');
var router = express.Router();

var handleErrors = require('../services/error-handler').handleErrors;
var sendInfo = require('../services/error-handler').sendInfo; 
var errorCodes = require('../services/error-codes').errorCodes;
var common = require('../services/common');
var config = require('../services/config');
var log = require('../services/log');
var Position = require('../services/mongo').Position;

router.get('/', function (req, res) {
  Position.find({}, function (err, positions) {
    if (err) {
      handleErrors(err, res, []);
    } else {
      sendInfo(errorCodes.Success, res, positions);
    }
  });
});

router.post('/', function (req, res) {
  var ip = req.body.ip;
  if (!ip) {
    sendInfo(errorCodes.IPEmpty, res, {});
    return;
  }

  var params = {
    qcip: ip,
    qterm: 'pc',
    ak: config.applicationAK,
    coord: 'bd09ll'
  };
  var opt = {
    method: 'GET',
    hostname: 'api.map.baidu.com',
    path: '/highacciploc/v1?' + qs.stringify(params)
  };

  var locReq = https.request(opt, function (locRes) {
    var responseString = '';

  	locRes.on('data', function(data) {
    	responseString += data;
  	});

    locRes.on('end', function () { 
        var resData = JSON.parse(responseString);
        if (resData.result.error != 161) {
          log.info('定位失败, 时间:' + resData.result.loc_time + ',错误码：' + resData.result.code);
          sendInfo(errorCodes.LocateError, res, {});
        } else {          
          sendInfo(errorCodes.Success, res, resData.content);
        }        
    }); 
  });

  locReq.end();
});

module.exports = router;