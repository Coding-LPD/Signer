var path = require('path');

var config = {

  // server
  port: 3000,

  // jwt
  signingKey: 'af51af55-d844-4568-87fc-7f2434ac0086',
  iss: 'http://www.signer.com',
  expire: 3600,  //3600s

  // mongo
  connection: 'mongodb://lpd:lpd@localhost/signer',
  debug: true,

  // 返回数据时，默认返回10条
  limit: 10,

  // 秒滴短信验证码
  // accountSID: '2e13f0c4838b4b239dbb64514863262b',
  // authToken: '145300409ae049189f7e79720432e816',

  // bmob短信验证码
  applicationId: 'fd44f39002f28f11e68f97adbe98b989',
  restApiKey: '1a911ff117742d4eb1e0566248f383b4',

  // 用户头像访问url前缀
  userImagesUrlPrefix: 'http://linkdust.xicp.net:50843/images/user/',
  // 用户头像物理地址
  userImagesPath: '/public/images/user',
  // 默认头像数量
  defaultImagesCount: 4,

  // connect-multiparty配置
  cmConfig: {
    uploadDir: path.resolve(__dirname, '../public/images')
  }  
}

module.exports = config;