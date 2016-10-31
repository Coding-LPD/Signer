var path = require('path');

var env = process.env.NODE_ENV || 'development';
env = env.toLowerCase();

var file = path.resolve(__dirname, env);
try {
  var config = module.exports = require(file);
  console.log('加载配置文件：%s.js', env);
} catch (err) {
  console.error('加载配置文件 %s.js 失败', env);
  throw err;
}