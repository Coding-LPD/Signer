var crypto = require('crypto');
var express = require('express');
var fs = require('fs');
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
  console.log(req.body);

  // 检查手机号码是否有效
  if (!common.validatePhone(phone)) {
    sendInfo(errorCodes.PhoneError, res, {});
    return;
  }

  // 查找手机号码是否已经注册过
  User.find({phone}, function (err, user) {
    if (err) {
      handleErrors(err, res, {});
      return;
    }
    // 用户已存在，注册失败
    if (user.length != 0) {
      sendInfo(errorCodes.UserExist, res, {});
      return;
    }

    // 根据用户提交的数据新建一个用户
    var newUser = new User({phone, password, role});
    // 读取私钥    
    var privatekeyPath = path.resolve(__dirname, '../services/privatekey.pem');
    fs.readFile(privatekeyPath, function (err, privatekey) {
      if (err) {
        handleErrors(err, res, {});
        return;
      }
      // 利用私钥解密用户提交的密码
      var key = new NodeRSA();
      key.importKey(privatekey);
      key.setOptions({encryptionScheme: 'pkcs1'});
      try {
        newUser.password = key.decrypt(newUser.password, 'utf8');
      } catch (err) {
        // 解密密码失败
        console.log(err);
        sendInfo(errorCodes.PasswordDecryptError, res, {});
        return;
      }
      // 将明文密码sha1加密
      var sha1 = crypto.createHash('sha1');
      sha1.update(newUser.password);
      newUser.password = sha1.digest('hex');
      // 保存用户
      newUser.save(function (err, savedUser) {
        if (err) {
          handleErrors(err, res, {});
          return;
        }
        var promise = [];
        // 用户成功注册
        if (savedUser.role === '0') {
          // 创建学生信息
          var student = new Student({phone});
          promise.push(student.save());
        } else {
          // 创建教师信息
          var teacher = new Teacher({phone});
          promise.push(teacher.save());
        }
        Promise.all(promise).then(function (savedData) {
          var id = savedData[0][0]._id;
          if (!id) {
            handleErrors(savedData[0], res, {});
            return;
          }
          // 返回认证token
          var token = jwtService.createToken(savedUser._id, 'student');
          res.cookie('access_token', token);
          sendInfo(errorCodes.Success, res, { user: {_id: savedUser._id, phone: savedUser.get('phone'), role: savedUser.get('role')}, id: id });
        });
      });
    });
  });
});

// 修改指定id的数据
router.put('/:id', function (req, res) {
  User.findByIdAndUpdate(req.params['id'], req.body, { new: true }, function (err, user) {
    if (!err) {
      sendInfo(errorCodes.Success, res, user);      
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
  
  // 参数错误
  if (!common.validatePhone(phone) || password.length == 0) {
    sendInfo(errorCodes.AuthInfoEmpty, res, {});
    return;
  }

  // 查找要登录的用户
  User.find({phone}, function (err, users) {    
    if (err) {
      handleErrors(err, res, {});
      return;
    }
    // 用户不存在
    if (users.length != 1) {
      sendInfo(errorCodes.AuthInfoError, res, {});
      return;
    }
    var user = users[0];
    // 读取私钥
    var privatekeyPath = path.resolve(__dirname, '../services/privatekey.pem');
    fs.readFile(privatekeyPath, function (err, privatekey) {
      if (err) {
        handleErrors(err, res, {});
        return;
      }
      // 解密密码      
      var key = new NodeRSA();      
      key.importKey(privatekey);
      key.setOptions({encryptionScheme: 'pkcs1'});
      try {
        password = key.decrypt(password);
      }
      catch (err) {
        // 解密密码失败
        console.log(err);
        sendInfo(errorCodes.PasswordDecryptError, res, {});
        return;
      }
      // sha1明文密码，并与数据库中的密码比对
      var sha1 = crypto.createHash('sha1');
      sha1.update(password);
      var r = sha1.digest('hex');
      // 密码不正确
      if (r !== user.password) {
        sendInfo(errorCodes.AuthInfoError, res, {});
        return;
      }      
      getId(user, function (err, id) {
        if (err) {
          handleErrors(err, res, {});
          return;
        }
        // 成功登录
        // 返回带有token的cookie
        var token = jwtService.createToken(user._id, 'student');
        res.cookie('access_token', token, { httpOnly: true });
        sendInfo(errorCodes.Success, res, { user: {_id: user._id, phone: user.get('phone'), role: user.get('role')}, id: id });
      });
    });
  });
});

// 验证码登录
router.post('/loginWithSmsCode', function (req, res) {
  var phone = req.body.phone || '';
  var smsCode = req.body.smsCode || '';

  // 手机号码无效
  if (!common.validatePhone(phone)) {
    sendInfo(errorCodes.PhoneError, res, {});
    return;
  }

  // 查找要登录的用户
  User.find({phone}, function (err, users) {
    if (err) {
      handleErrors(err, res, {});
      return;
    }
    // 用户不存在
    if (users.length != 1) {
      sendInfo(errorCodes.UserNotExist, res, {});
      return;
    }
    verifySmsCode(phone, smsCode, res, function (err, data) {
      // 验证成功，则说明用户登录成功
      if (!err) {
        var user = users[0];
        getId(user, function (err, id) {
          if (err) {
            handleErrors(err, res, {});
            return;
          }
          // 返回带有token的cookie
          var token = jwtService.createToken(user._id, 'student');
          res.cookie('access_token', token, { httpOnly: true });
          sendInfo(errorCodes.Success, res, { user: {_id: user._id, phone: user.get('phone'), role: user.get('role')}, id: id });
        });        
      }
    });
  }); 
});

function getId(user, callback) {
  var phone = user.get('phone');
  var role = user.get('role');
  var promise = [];
  if (+role == 0) {
    promise.push(Student.find({ phone }));
  } else {
    promise.push(Teacher.find({ phone }));
  }
  Promise.all(promise).then(function (findedData) {
    var id = findedData[0][0]._id;
    if (id) {
      callback(null, id);
    } else {
      callback(findedData[0]);
    }
  });
}

module.exports = router;
