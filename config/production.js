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
  // debug: true,

  // 返回数据时，默认返回10条
  limit: 10,

  // 百度服务端AK
  applicationAK: 'fLHlHHLy4lrss2hY1GNsdSdb1y8FOwZ3',

  // bmob短信验证码
  applicationId: 'fd44f39002f28f11e68f97adbe98b989',
  restApiKey: '1a911ff117742d4eb1e0566248f383b4',

  // 用户头像访问url前缀
  userImagesUrlPrefix: 'http://120.25.65.207:3000/images/user/',
  // 用户头像物理地址
  userImagesPath: '/public/images/user',
  // 默认头像数量
  defaultImagesCount: 4,

  // 文件下载地址
  fileDownload: 'http://120.25.65.207:3000/temp/',

  // connect-multiparty配置
  cmConfig: {
    uploadDir: path.resolve(__dirname, '../public/images')
  }
}

module.exports = config;