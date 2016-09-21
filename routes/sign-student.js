var express = require('express');
var fs = require('fs');
var xlsx = require('xlsx');
var multipart = require('connect-multiparty');
var moment = require('moment');
var router = express.Router();
var multipartMiddleware = multipart();

var handleErrors = require('../services/error-handler').handleErrors;
var sendInfo = require('../services/error-handler').sendInfo;
var errorCodes = require('../services/error-codes').errorCodes;
var common = require('../services/common');
var Course = require('../services/mongo').Course;
var SignStudent = require('../services/mongo').SignStudent;

router.get('/', function (req, res) {
  SignStudent.find(function (err, students) {
    if (err) {
      handleErrors(err, res, []);      
    } else {
      sendInfo(errorCodes.Success, res, students);
    }
  });
});

router.post('/search', function (req, res) {
  // 必须要有查询条件
  if (common.isEmptyObject(req.body)) {
    sendInfo(errorCodes.SearchEmpty, res, []);
    return;
  }
  SignStudent.find(req.body, function (err, signStudents) {
    if (!err) {
      sendInfo(errorCodes.Success, res, signStudents);
    } else {
      handleErrors(err, res, []);
    }
  });
});

router.post('/import', multipartMiddleware, function (req, res) {
  var courseId = req.body.courseId;

  var promises = [];
  var file = req.files.fieldNameHere || req.files.file;  
  var workbook = xlsx.readFile(file.path);
  var worksheet = workbook.Sheets[workbook.SheetNames[0]];  
  var courseExtra = readHeader(worksheet);
  var signStudents = readBody(worksheet); 
  Course.find({_id: courseId}, function (err, courses) {
    if (err) {
      handleErrors(err, res, []);
      return;
    }
    var course = courses[0];
    course.set('location', courseExtra.get('location'));
    course.set('time', courseExtra.get('time'));
    course.set('academy', courseExtra.get('academy'));         
    course.set('studentCount', course.get('studentCount') + signStudents.length);
    promises.push(course.save());

    for (var i=0; i<signStudents.length; i++) {
      signStudents[i].set('teacherId', course.get('teacherId'));
      signStudents[i].set('courseId', courseId);
      signStudents[i].set('phone', '');     
      signStudents[i].set('createdAt', moment(new Date()).format('YYYY-MM-DD'));  
      promises.push(signStudents[i].save());
    }

    Promise.all(promises).then(function (savedData) {
      sendInfo(errorCodes.Success, res, savedData.slice(1));
    }, function(err) {
      handleErrors(err, res, []);
    })
  })  

  // 删除文件
  fs.unlink(file.path, function (err) {
    if (err) {
      handleErrors(err, res, []);
      return;
    }
  })  
});

function readHeader(worksheet) { 
  var course = new Course({
    location: worksheet['N3'].v,
    time: worksheet['J3'].v,
    academy: worksheet['E3'].v
  });
  return course;
}

function readBody(worksheet) {
  var signStudents = [];
  var startRow = 5;
  var i = 'A' + startRow;
  while (worksheet[i]) {
    signStudents[signStudents.length] = new SignStudent({
      number: worksheet['A' + startRow].v,
      name: worksheet['C' + startRow].v,
      major: worksheet['E' + startRow].v
    });
    i = 'A' + ++startRow;
  }
  return signStudents;
}

module.exports = router;