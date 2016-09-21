var fs = require('fs');
var path = require('path');
var multipart = require('connect-multiparty');
var express = require('express');
var router = express.Router();

var handleErrors = require('../services/error-handler').handleErrors;
var sendInfo = require('../services/error-handler').sendInfo; 
var errorCodes = require('../services/error-codes').errorCodes;
var common = require('../services/common');
var config = require('../services/config');
var multipartMiddleware = multipart(config.cmConfig);
var Student = require('../services/mongo').Student;

router.get('/', function (req, res) {
  Student.find(function (err, students) {
    if (!err) {
      sendInfo(errorCodes.Success, res, students);
    } else {
      handleErrors(err, res, []);
    }
  });
});

router.put('/:id', function (req, res) {  
  delete req.body._id;
  // 禁止修改学生手机号码
  delete req.body.phone;
  Student.findByIdAndUpdate(req.params['id'], req.body, { new: true }, function (err, newStudent) {
    if (!err) {
      console.log(newStudent);
      sendInfo(errorCodes.Success, res, newStudent);
    } else {
      handleErrors(err, res, {});
    }
  });
});

router.post('/search', function (req, res) {
  // 必须要有查询条件
  if (common.isEmptyObject(req.body)) {
    sendInfo(errorCodes.SearchEmpty, res);
    return;
  }
  Student.find(req.body, function (err, students) {
    if (!err) {
      sendInfo(errorCodes.Success, res, students);
    } else {
      handleErrors(err, res, []);
    }
  });
});

router.post('/images', multipartMiddleware, function (req, res) {
  var file;
  for (var prop in req.files) {
    file = req.files[prop];
    break;
  }
  if (!file) {
    sendInfo(errorCodes.NoFileError, res, '');
    return;
  }

  var extension = file.originalFilename.slice(file.originalFilename.lastIndexOf('.'));
  var fileName = common.generateRandomStr(3) + extension; 
  var newPath = path.resolve(__dirname, '..' + config.userImagesPath);  
  newPath += '\\' + fileName;
  fs.rename(file.path, newPath, function (err) {
    if (!err) {
      var url = config.userImagesUrlPrefix + fileName;
      sendInfo(errorCodes.Success, res, url);
    } else {
      handleErrors(err, res, '');
    } 
  });
});

router.get('/images/:id', function (req, res) {
  var id = req.params['id'];
  if (id < 1 || id > config.defaultImagesCount) {
    sendInfo(errorCodes.NoDefaultImageError, res, '');   
  } else {
    sendInfo(errorCodes.Success, res, config.userImagesUrlPrefix + id + '.png');
  }
});

module.exports = router;