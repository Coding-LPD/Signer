var log = require('./log');
var errorInfo = require('./error-codes');

function handleErrors(err, res, data) {
  res.send(wrapError(err, data));  
}

function sendInfo(code, res, data) {  
  res.send(wrapData(code, data));
}

function wrapData(code, data) {
  return { code: code, data: data, msg: errorInfo.errorMsg[code] };
}

function wrapError(err, data) {
  // 使用内部err对象返回信息
  if (err.errors) {
    // 获取第一个验证器的错误信息并返回    
    for (var key in err.errors) {
      var code = err.errors[key].message;
      var message = errorInfo.errorMsg[code];
      log.info(code + ': ' + message);
      return { code: code, data: data, msg: message };
    }
  } else {
    // 其他内部错误  
    log.info(err);
    return { code: errorInfo.errorCodes.OtherError, data: data, msg: '服务器内部出错' };
  }
}

exports.handleErrors = handleErrors;
exports.sendInfo = sendInfo;
exports.wrapData = wrapData;
exports.wrapError = wrapError;