var rp = require('request-promise');
var crypto = require('crypto');
var querystring = require('querystring');
var express = require('express');
var router = express.Router();

var config = require('../config');
var sendInfo = require('../services/error-handler').sendInfo;
var errorCodes = require('../services/error-codes').errorCodes;
var common = require('../services/common');
var log = require('../services/log');

var User = require('../services/mongo').User;

router.get('/', function(req, res, next) {
  res.render('index', { title: 'SmsCode' });
});

// 发送短信验证码
router.post('/', function(req, res) {
  var phone = req.body.phone;
  var data = {
    mobilePhoneNumber: phone
  }
  var dataString = JSON.stringify(data);  

  Promise.resolve()
    .then(function () {
      // 手机号码无效
      if (!common.validatePhone(phone)) {        
        return Promise.reject({ code: errorCodes.PhoneError }) ;
      }

      var options = {
        method: 'POST',
        uri: 'https://api.bmob.cn/1/requestSmsCode',
        body: data,
        headers: {
          'X-Bmob-Application-Id': config.applicationId,
          'X-Bmob-REST-API-Key': config.restApiKey,
          'Content-Type': 'application/json',
          'Content-Length': dataString.length
        },
        json: true
      };

      return rp(options);
    })
    .then(function (data) {
      sendInfo(errorCodes.Success, res, data.smsId);
    })
    .catch(function (err) {
      var result = handleBmobError(err.error);
      if (result.code) {
        sendInfo(result.code, res, result.bmobError || '');
      } else {
        handleErrors(err, res, '');
      }
    })
});

// 验证短信验证码
router.post('/verification', function (req, res) {
  var smsCode = req.body.smsCode;
  var phone = req.body.phone;

  Promise.resolve()
    .then(function () {
      // 手机号码无效
      if (!common.validatePhone(phone)) {
        return Promise.reject({ code: errorCodes.PhoneError }) ;
      }

      return verifySmsCode(phone, smsCode);
    })
    .then(function (data) {
      sendInfo(errorCodes.Success, res, '');
    })
    .catch(function (err) {
      if (err.code) {
        sendInfo(err.code, res, err.bmobError || '');
      } else {
        handleErrors(err, res, '');
      }
    });
});

// 查询短信状态
router.get('/state', function (req, res) {
  var smsid = req.query.smsid;

  getSmsCodeState(smsid)
    .then(function (data) {
      sendInfo(errorCodes.Success, res, data);
    })
    .catch(function (err) {
      if (err.code) {
        sendInfo(err.code, res, err.bmobError || '');
      } else {
        handleErrors(err, res, '');
      }
    });
});

/**
 * bmob验证短信验证码
 */
function verifySmsCode(phone, smsCode) {
  var data = {
    mobilePhoneNumber: phone
  };
  var dataString = JSON.stringify(data);

  var options = {
    method: 'POST',
    uri: 'https://api.bmob.cn/1/verifySmsCode/' + smsCode,
    body: data,
    headers: {
      'X-Bmob-Application-Id': config.applicationId,
      'X-Bmob-REST-API-Key': config.restApiKey,
      'Content-Type': 'application/json',
      'Content-Length': dataString.length
    },
    json: true
  };

  return rp(options)
    .then(function (data) {
      return data;
    })
    .catch(function (err) {
      return Promise.reject(handleBmobError(err.error));
    });
}

function getSmsCodeState(smsid) {
  var options = {
    method: 'GET',
    uri: 'https://api.bmob.cn/1/querySms/:' + smsid,
    headers: {
      'X-Bmob-Application-Id': config.applicationId,
      'X-Bmob-REST-API-Key': config.restApiKey,
      'Content-Type': 'application/json'
    },
    json: true
  };

  return rp(options)
    .then(function (data) {
      return {
        sendState: data.sms_state,
        verifyState: data.verify_state
      };
    })
    .catch(function (err) {
      return Promise.reject(handleBmobError(err.error));
    });
}

function handleBmobError(data) {
  var result = { code: errorCodes.OtherError, bmobError: null };
  if (!data.code) {
    return result;
  }
  switch(data.code) {
    case 207:
      // 验证码错误或已被验证过
      result.code = errorCodes.SmsCodeError;
      break;
    case 301:
      // 手机号码不正确
      result.code = errorCodes.PhoneError;
      break;
    case 10010:
      result.code = errorCodes.SmsCodeLimit;
      break;
    default:      
      // bmob其他错误，直接返回bmob的错误提示
      result.code = errorCodes.BmobOtherError;
      result.bmobError = data.error;
      break;
  }
  return result;
}

exports.router = router;
exports.verifySmsCode = verifySmsCode;
exports.getSmsCodeState = getSmsCodeState;