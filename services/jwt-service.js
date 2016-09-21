var nJwt = require('njwt');
var config = require('../services/config');
var log = require('./log');
var sendInfo = require('../services/error-handler').sendInfo;
var errorCodes = require('./error-codes').errorCodes;
// 生成signingKey方法
// var uuid = require('uuid');
// var key = uuid.v4();

function JwtService() {}

JwtService.createToken = function (userId, scope) {
  var claims = {
    iss: config.iss,  
    sub: userId,    
    scope: scope
  }
  var jwt = nJwt.create(claims, config.signingKey);  
  jwt.setExpiration(new Date().getTime() + (config.expire*1000));
  var token = jwt.compact();
  return token;
}

JwtService.authUser = function (errorReturn) {
    return function (req, res, next) {
    var token = req.cookies['access_token'];
    if (!token) {
      sendInfo(errorCodes.AuthenticationError, res, errorReturn);
      return;
    }
    nJwt.verify(token, config.signingKey, function (err, verifiedJwt) {
      if (err) {
        log.info('verify jwt error: ' + err.message);
        // 验证token出错时，如果parsedBody不为null，说明为token超时
        // 如果为null，说明解析有误，token有错误或被修改过
        if (err.parsedBody) {
          sendInfo(errorCodes.AuthenticationExpired, res, errorReturn);   
        } else {
          sendInfo(errorCodes.AuthenticationError, res, errorReturn);
        }      
      } else {
        req.claims = verifiedJwt.body;
        // 检查token中的信息是否被修改
        if (req.claims.iss !== config.iss) {
          sendInfo(errorCodes.AuthenticationError, res, errorReturn);
          return;
        }
        next();
      }    
    });
  }
}

module.exports = JwtService;