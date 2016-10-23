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

Common.EARTH_RADIUS = 6378137.0;    //单位M
Common.PI = Math.PI;

Common.getRad = function (d){
    return d*Common.PI/180.0;
}

/**
 * approx distance between two points on earth ellipsoid
 * @param {Object} lat1
 * @param {Object} lng1
 * @param {Object} lat2
 * @param {Object} lng2
 */
Common.getFlatternDistance = function (lat1,lng1,lat2,lng2){
    var f = Common.getRad((lat1 + lat2)/2);
    var g = Common.getRad((lat1 - lat2)/2);
    var l = Common.getRad((lng1 - lng2)/2);
    
    var sg = Math.sin(g);
    var sl = Math.sin(l);
    var sf = Math.sin(f);
    
    var s,c,w,r,d,h1,h2;
    var a = Common.EARTH_RADIUS;
    var fl = 1/298.257;
    
    sg = sg*sg;
    sl = sl*sl;
    sf = sf*sf;
    
    s = sg*(1-sl) + (1-sf)*sl;
    c = (1-sg)*(1-sl) + sf*sl;
    
    w = Math.atan(Math.sqrt(s/c));
    r = Math.sqrt(s*c)/w;
    d = 2*w*a;
    h1 = (3*r -1)/2/c;
    h2 = (3*r +1)/2/s;
    
    return d*(1 + fl*(h1*sf*(1-sg) - h2*(1-sf)*sg));
}

// Common.isImage = function (path) {
//   var magic = new Magic(mmm.MAGIC_MIME_TYPE);
//   magic.detectFile(path, function(err, result) {
//       if (err) throw err;
//       console.log(result);
//   });
// }

module.exports = Common;