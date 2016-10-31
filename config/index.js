var path = require('path');

var env = process.env.NODE_ENV || 'development';
env = env.toLowerCase();

var file = path.resolve(__dirname, env);
try {
  var config = module.exports = require(file);
  console.log('load: %s.js', env);
} catch (err) {
  console.error('load fail: %s.js', env);
  throw err;
}