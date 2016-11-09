var rp = require('request-promise');
var qs = require('querystring');
var express = require('express');
var router = express.Router();

var handleErrors = require('../services/error-handler').handleErrors;
var sendInfo = require('../services/error-handler').sendInfo; 
var errorCodes = require('../services/error-codes').errorCodes;
var common = require('../services/common');
var config = require('../config');
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
  var signId = req.body.signId;
  var teacherId = req.body.teacherId;
  var ip = req.body.ip;  
  if (!ip) {
    ip = common.getClientIp(req);
  }

  Position.findOne({ signId: signId, teacherId: teacherId })
    .then(function (position) {      
      if (position) {
        // 定位过了则不再重新定位
        return Promise.reject({ code: errorCodes.Success, data: position });        
      } else {
        // 调用百度高精度IP定位api
        var options = {
          method: 'GET',
          uri: 'https://api.map.baidu.com/highacciploc/v1?',          
          qs: {            
            qcip: ip,
            qterm: 'pc',
            ak: config.applicationAK,
            coord: 'bd09ll'
          },
          json: true
        };
        return rp(options);
      }
    })
    .then(function (data) {
      // 定位失败
      if (data.result.error != 161) {
        return Promise.reject({ code: errorCodes.LocateError });
      }

      // 保存第一次定位信息
      var pos = {
        longitude: data.content.location.lng,
        latitude: data.content.location.lat
      };      
      return Position.findOneAndUpdate({ signId: signId, teacherId: teacherId }, pos, { upsert: true, new: true })
    })
    .then(function (position) {
      sendInfo(errorCodes.Success, res, position);
    })
    .catch(function (err) {
      if (err.code) {
        sendInfo(err.code, res, err.data || {});
      } else {
        handleErrors(err, res, {});
      }
    });  
});

router.put('/:id', function (req, res) {
  Position.findByIdAndUpdate(req.params['id'], req.body, { new: true }, function (err, savedPosition) {
    if (err) {
      handleErrors(errorCodes.OtherError, res, {});
    } else {
      sendInfo(errorCodes.Success, res, savedPosition);
    }
  });
});

module.exports = router;