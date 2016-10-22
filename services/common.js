// var mmm = require('mmmagic'),
// Magic = mmm.Magic;

function Common() {}

// 检查手机号码是否有效
Common.validatePhone = function (phone) {
  var myreg = /^(((13[0-9]{1})|(15[0-9]{1})|(18[0-9]{1}))+\d{8})$/; ;
  phone = phone || '';
  phone = phone.trim();

  if (phone.length == 0 || phone.length != 11) {
    return false;
  }

  if(!myreg.test(phone)) {
    return false;
  }

  return true;
}

// 判断对象是否为({})
Common.isEmptyObject = function(obj) {
  for (var t in obj) 
    return false;
  return true;
}

// 获取0~range的一个随机数
Common.getRandomNum = function (range) {
  var result = Math.floor(Math.random() * 1000) % range;
  return result;
}

// 根据当前时间戳生成随机串
Common.generateRandomStr = function (suffixLength) {
  var result = new Date().getTime().toString();
  for (var i=0; i<suffixLength; i++) {
    result += Common.getRandomNum(10);
  }
  return result;
}

Common.getClientIp = function (req) {
    return req.headers['x-forwarded-for'] ||
      req.connection.remoteAddress ||
      req.socket.remoteAddress ||
      req.connection.socket.remoteAddress;
};

// Common.isImage = function (path) {
//   var magic = new Magic(mmm.MAGIC_MIME_TYPE);
//   magic.detectFile(path, function(err, result) {
//       if (err) throw err;
//       console.log(result);
//   });
// }

module.exports = Common;