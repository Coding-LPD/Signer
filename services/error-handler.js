var log = require('./log');
var errorInfo = require('./error-codes');

function handleErrors(err, res, data) {
  // 使用内部err对象返回信息
  if (err.errors) {
    // 获取第一个验证器的错误信息并返回    
    for (var key in err.errors) {
      var code = err.errors[key].message;
      var message = errorInfo.errorMsg[code];
      log.info(code + ': ' + message);
      res.send({ code: code, data: data, msg: message });
      break;
    }
  } else {
    // 其他内部错误
    // log.info('Internal error ' + res.statusCode + ' : ' + err.message);  
    log.info(err);
    res.send({ code: errorInfo.errorCodes.OtherError, data: data, msg: '服务器内部出错' });
  }
}

function sendInfo(code, res, data) {  
  // log.info(code + ': ' + errorInfo.errorMsg[code]);
  // data = data || errorInfo.errorMsg[code];
  res.send({ code: code, data: data, msg: errorInfo.errorMsg[code] });
  res.end();
}

exports.handleErrors = handleErrors;
exports.sendInfo = sendInfo;