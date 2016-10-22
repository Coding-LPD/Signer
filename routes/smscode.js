var https = require('https');
var crypto = require('crypto');
var querystring = require('querystring');
var express = require('express');
var router = express.Router();
var config = require('../services/config');
var sendInfo = require('../services/error-handler').sendInfo;
var errorCodes = require('../services/error-codes').errorCodes;
var common = require('../services/common');
var log = require('../services/log');

var User = require('../services/mongo').User;

router.get('/', function(req, res, next) {
  res.render('index', { title: 'SmsCode' });
});

router.get('/h', function (req, res) {
  res.status(405);
  res.end();
});

// 发送短信验证码
router.post('/', function(req, res) {
  var phone = req.body.phone;  
  if (!common.validatePhone(phone)) {
    sendInfo(errorCodes.PhoneError, res, '');
    return;
  }
  var data = {
    mobilePhoneNumber: phone
  }
  var dataString = JSON.stringify(data);

  var opt = {
    method: 'POST',
    hostname: 'api.bmob.cn',
    path: '/1/requestSmsCode',
    headers: {
      'X-Bmob-Application-Id': config.applicationId,
      'X-Bmob-REST-API-Key': config.restApiKey,
      'Content-Type': 'application/json',
      'Content-Length': dataString.length
    }
  };

  var smscodeReq = https.request(opt, function (smscodeRes) {
    var responseString = '';

    smscodeRes.on('data', function(data) {
      responseString += data;
    });

    smscodeRes.on('end', function () {         
        var resData = JSON.parse(responseString);
        if (resData.error) {
          handleBmobError(resData, res);
        } else {
          sendInfo(errorCodes.Success, res, resData.smsId);
        }
    }); 
  });

  smscodeReq.on('error', function(e) {
      console.log('request bmob smscode error: \n',e);
      sendInfo(errorCodes.OtherError, res, '');
  });

  smscodeReq.write(dataString);
  smscodeReq.end();
});

// 验证短信验证码
router.post('/verification', function (req, res) {
  var smsCode = req.body.smsCode;
  var phone = req.body.phone;
  if (!common.validatePhone(phone)) {
    sendInfo(errorCodes.PhoneError, res, '');
    return;
  }
  
  verifySmsCode(phone, smsCode, res, function (err, data) {
    if (!err) {
      sendInfo(errorCodes.Success, res, '');
    }
  });

});

// 查询短信状态
router.get('/state', function (req, res) {
  var smsid = req.query.smsid;

  getSmsCodeState(smsid, res, function (err, data) {
    // 请求成功，则返回相应短信状态数据
    if (!err) {
      sendInfo(errorCodes.Success, res, data);
    }
  });  
});

function verifySmsCode(phone, smsCode, res, callback) {
  var data = {
    mobilePhoneNumber: phone
  };
  var dataString = JSON.stringify(data);

  var opt = {
    method: 'POST',
    hostname: 'api.bmob.cn',
    path: '/1/verifySmsCode/' + smsCode,
    headers: {
      'X-Bmob-Application-Id': config.applicationId,
      'X-Bmob-REST-API-Key': config.restApiKey,
      'Content-Type': 'application/json',
      'Content-Length': dataString.length
    }
  };

  var verificationReq = https.request(opt, function (verificationRes) {
    var responseString = '';

  	verificationRes.on('data', function(data) {
    	responseString += data;
  	});

    verificationRes.on('end', function () {         
        var resData = JSON.parse(responseString);        
        if (resData.error) {     
          handleBmobError(resData, res);
          callback(resData);
        } else {               
          callback(null, resData);          
        }        
    }); 
  });

  verificationReq.write(dataString);
  verificationReq.end();
}

function getSmsCodeState(smsid, res, callback) {
  var opt = {
    method: 'GET',
    hostname: 'api.bmob.cn',
    path: '/1/querySms/' + smsid,
    headers: {
      'X-Bmob-Application-Id': config.applicationId,
      'X-Bmob-REST-API-Key': config.restApiKey,
      'Content-Type': 'application/json'
    }
  };

  var stateReq = https.request(opt, function (stateRes) {
    var responseString = '';

  	stateRes.on('data', function(data) {
    	responseString += data;
  	});

    stateRes.on('end', function () { 
        var resData = JSON.parse(responseString);
        if (resData.error) {          
          log.info('bmob return msg: ' + resData.error);
          sendInfo(errorCodes.SmsidError, res, '');
          callback(resData);
        } else {
          resData = {
            sendState: resData.sms_state,
            verifyState: resData.verify_state
          };
          callback(null, resData);
        }
    }); 
  });

  stateReq.end();
}

function handleBmobError(resData, res) {
  if (!resData.code) {
    sendInfo(errorCodes.OtherError, res, '');
    return;
  }
  switch(resData.code) {
    case 207:
      // 验证码错误或已被验证过
      sendInfo(errorCodes.SmsCodeError, res, '');
      break;
    case 301:
      // 手机号码不正确
      sendInfo(errorCodes.PhoneError, res, '');
      break;
    case 10010:
      sendInfo(errorCodes.SmsCodeLimit, res, '');
      break;
    default:      
      // bmob其他错误，直接返回bmob的错误提示
      sendInfo(errorCodes.BmobOtherError, res, resData.error);
      break;
  }
}


// // 秒滴发送短信
// function miaodi(req, res) {
//   var timestamp = getTimeStamp();
//   var md5sum = crypto.createHash('md5');
//   md5sum.update(config.accountSID + config.authToken + timestamp);
//   var sig = md5sum.digest('hex');
//   console.log('sig: ' + sig);

//   var data = {
//     accountSid: config.accountSID,
//     smsContent: '【秒嘀科技】您的验证码是345678，30分钟输入有效。',
//     to: '15603005627',
//     timestamp: getTimeStamp(),
//     sig: sig
//   };
//   data = querystring.stringify(data);

//   var opt = {
//     method: 'POST',
//     host: 'api.miaodiyun.com',
//     path: '/20150822/industrySMS/sendSMS',
//     headers: {
//       'Content-Type': 'application/x-www-form-urlencoded'
//     }
//   };

//   var req = http.request(opt, function (res) {
//     console.log('STATUS: ' + res.statusCode);  
//     console.log('HEADERS: ' + JSON.stringify(res.headers));
//     res.on('data', function (chunk) {  
//         console.log('BODY: ' + chunk);  
//     }); 
//   });

//   req.write(data);
//   req.end();
// }

// function getTimeStamp() {
//   var d = new Date();
//   var year = d.getFullYear();
//   var month = d.getMonth() + 1;    
//   var day = d.getDate();
//   var hour = d.getHours();
//   var minute = d.getMinutes();
//   var second = d.getSeconds(); 
//   if (month < 10) {
//     month = '0' + month;
//   }
//   if (day < 10) {
//     day = '0' + day;
//   }
//   if (hour < 10) {
//     hour = '0' + hour;
//   } 
//   if (minute < 10) {
//     minute = '0' + minute;
//   }
//   if (second < 10) {
//     second = '0' + second;
//   }
//   console.log('timestamp: ' + year + month + day + hour + minute + second);
//   return '' + year + month + day + hour + minute + second;
// }

exports.router = router;
exports.verifySmsCode = verifySmsCode;
exports.getSmsCodeState = getSmsCodeState;