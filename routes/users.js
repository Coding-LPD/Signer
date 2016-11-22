var crypto = require('crypto');
var express = require('express');
var fs = require('fs');
var fsp = require('fs-promise');
var path = require('path');
var NodeRSA = require('node-rsa');
var router = express.Router();

var log = require('../services/log');
var jwtService = require('../services/jwt-service');
var handleErrors = require('../services/error-handler').handleErrors;
var sendInfo = require('../services/error-handler').sendInfo; 
var errorCodes = require('../services/error-codes').errorCodes;
var common = require('../services/common');
var verifySmsCode = require('./smscode').verifySmsCode;

var User = require('../services/mongo').User;
var Student = require('../services/mongo').Student;
var Teacher = require('../services/mongo').Teacher;
var returnUserField = '-password';

// 获取全部数据
// router.get('/', jwtService.authUser, function(req, res) {
router.get('/', function(req, res) {
  User.find(function (err, users) {
    if (!err) {
      sendInfo(errorCodes.Success, res, users);
    } else {
      handleErrors(err, res, []);
    }
  });
});

// 获取指定id数据
router.get('/:id', function (req, res) {
  User.findById(req.params['id'], function (err, user) {
    if (!err) {
      sendInfo(errorCodes.Success, res, user);
    } else {
      handleErrors(err, res, {});
    }
  });
});

// 用户注册
router.post('/', function (req, res) {
  var phone = req.body.phone;
  var password = req.body.password;
  var role = req.body.role;
  var user;

  Promise.resolve()
    .then(function () {
      // 检查手机号码是否有效
      if (!common.validatePhone(phone)) {
        return Promise.reject({ code: errorCodes.PhoneError });
      }

      return User.find({ phone: phone });
    })
    .then(function (users) {
      // 用户已存在，注册失败
      if (users.length != 0) {        
        return Promise.reject({ code: errorCodes.UserExist });
      }      

      // 读取私钥 
      return readKey(1);
    })
    .then(function (privatekey) {
      // 利用私钥解密用户提交的密码
      return decryptContent(password, privatekey);
    })
    .then(function (oriPassword) {
      // 加密密码
      password = sha1Content(oriPassword);

      // 根据用户提交的数据新建一个用户
      var user = new User({
        phone: phone, 
        password: password, 
        role: role
      });

      return user.save();
    })
    .then(function (savedUser) {
      user = savedUser;      
      /**
       * role
       * 1：创建教师信息
       * 其他：创建学生信息
       */
      if (+savedUser.get('role') == 1) {
        return new Teacher({ phone: phone }).save();
      } else {
        return new Student({ phone: phone }).save();
      }
    })
    .then(function (savedPerson) {
      // token可使用范围
      var scope = +user.get('role') == 1 ? 'teacher' : 'student';
      // 返回认证token
      var token = jwtService.createToken(user._id, scope);
      res.cookie('access_token', token, { httpOnly: true });
      sendInfo(errorCodes.Success, res, 
        { 
          user: { _id: user._id, phone: user.get('phone'), role: user.get('role') }, 
          person: savedPerson
        }
      );      
    })
    .catch(function (err) {
      if (err.code) {
        sendInfo(err.code, res, {});
      } else {
        handleErrors(err, res, {});
      }
    });
});

// 修改指定手机号码的用户数据
router.put('/:phone', function (req, res) {
  var phone = req.params['phone'];  
  var newPassword = req.body.password;
  var user;

  User.findOne({ phone: phone })
    .then(function (findedUser) {
      if (!findedUser) {
        return Promise.reject({ code: errorCodes.UserNotExist });
      }
      user = findedUser;

      // 读取私钥
      return readKey(1);
    })
    .then(function (privatekey) {
      // 解密密码
      return decryptContent(newPassword, privatekey);
    })
    .then(function (password) {
      // sha1加密密码
      newPassword = sha1Content(password);

      // 更新用户密码
      user.set('password', newPassword);
      return user.save();
    })
    .then(function (updatedUser) {
      // 返回的新的user信息不包含密码
      updatedUser = updatedUser.toObject();
      delete updatedUser.password;
      sendInfo(errorCodes.Success, res, updatedUser);
    })
    .catch(function (err) {
      if (err.code) {
        sendInfo(err.code, res, {});
      } else {
        handleErrors(err, res, {});
      }
    });
    
});

// 删除指定id的数据
router.delete('/:id', function (req, res) {
  User.findByIdAndRemove(req.params['id'], function (err, user) {
    if (!err) {
      sendInfo(errorCodes.Success, res, user);
    } else {
      handleErrors(err, res, {});
    }
  });
});

// 查找数据
router.post('/search', function (req, res) {
  User.find(req.body, returnUserField, function (err, users) {
    if (!err) {
      sendInfo(errorCodes.Success, res, users);      
    } else {
      handleErrors(err, res, []);
    }
  });
});

// 密码登录
router.post('/login', function (req, res) {
  var phone = req.body.phone || '';
  var password = req.body.password || '';  
  var user;
  
  Promise.resolve()
    .then(function () {
      // 参数错误
      if (!common.validatePhone(phone) || password.length == 0) {
        return Promise.reject({ code: errorCodes.AuthInfoEmpty });
      }

      return User.find({ phone });
    }) 
    .then(function (users) {
      // 用户不存在
      if (users.length != 1) {
        return Promise.reject({ code: errorCodes.AuthInfoError });
      }

      user = users[0];
      return readKey(1);
    })
    .then(function (privatekey) {
      return decryptContent(password, privatekey);
    })
    .then(function (oriPassword) {
      var password = sha1Content(oriPassword);
      // 密码不正确
      if (password !== user.get('password')) {        
        return Promise.reject({ code: errorCodes.AuthInfoError });
      }
      /**
       * role
       * 1：查询教师信息
       * 其他：查询学生信息
       */
      if (+user.get('role') == 1) {
        return Teacher.findOne({ phone: phone });
      } else {
        return Student.findOne({ phone: phone });
      }
    })
    .then(function (person) {
      // token可使用范围
      var scope = +user.get('role') == 1 ? 'teacher' : 'student';
      // 返回带有token的cookie
      var token = jwtService.createToken(user._id, scope);
      res.cookie('access_token', token, { httpOnly: true });
      sendInfo(errorCodes.Success, res,
        { 
          user: { _id: user._id, phone: user.get('phone'), role: user.get('role') }, 
          person: person
        }
      );
    })
    .catch(function (err) {
      if (err.code) {
        sendInfo(err.code, res, {});
      } else {
        handleErrors(err, res, {});
      }
    });
});

// 验证码登录
router.post('/loginWithSmsCode', function (req, res) {
  var phone = req.body.phone || '';
  var smsCode = req.body.smsCode || '';
  var user;

  Promise.resolve()
    .then(function () {
      // 手机号码无效
      if (!common.validatePhone(phone)) {
        return Promise.reject({ code: errorCodes.PhoneError });
      }

      return User.find({ phone: phone });
    })
    .then(function (users) {
      // 用户不存在
      if (users.length != 1) {        
        return Promise.reject({ code: errorCodes.UserNotExist });
      }
      user = users[0];

      return verifySmsCode(phone, smsCode);
    })
    .then(function (data) {
      /**
       * role
       * 1：查询教师信息
       * 其他：查询学生信息
       */
      if (+user.get('role') == 1) {
        return Teacher.findOne({ phone: phone });
      } else {
        return Student.findOne({ phone: phone });
      }
    })
    .then(function (person) {
      // token可使用范围
      var scope = +user.get('role') == 1 ? 'teacher' : 'student';
      // 返回带有token的cookie
      var token = jwtService.createToken(user._id, scope);
      res.cookie('access_token', token, { httpOnly: true });
      sendInfo(errorCodes.Success, res,
        { 
          user: { _id: user._id, phone: user.get('phone'), role: user.get('role') }, 
          person: person
        }
      );
    })
    .catch(function (err) {
      if (err.code) {
        sendInfo(err.code, res, err.bmobError || {});
      } else {
        handleErrors(err, res, {});
      }
    });
});

/**
 * 读取密钥
 * type：0公钥，1私钥
 */
function readKey(type) {
  type = type || 0;  
  var keyPath = type == 1 ? 'privatekey.pem' : 'publickey.pem';
  keyPath = path.resolve(__dirname, '../services/' + keyPath);

  return fsp.readFile(keyPath);
}

/**
 * 利用指定密钥解密指定内容，默认加密模式为pkcs1，编码格式为utf8
 */
function decryptContent(content, key) {
  var rsa = new NodeRSA();
  rsa.importKey(key);
  rsa.setOptions({encryptionScheme: 'pkcs1'});
  try {
    result = rsa.decrypt(content, 'utf8');
    return Promise.resolve(result);
  } catch (err) {
    // 解密密码失败
    return Promise.reject({ code: errorCodes.PasswordDecryptError });        
  }
}

/**
 * 将指定进行sha1加密
 */
function sha1Content(content) {
  var sha1 = crypto.createHash('sha1');
  sha1.update(content);
  return sha1.digest('hex');
}

module.exports = router;
