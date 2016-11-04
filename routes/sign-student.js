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

router.delete('/:id', function (req, res) {  
  SignStudent.findByIdAndRemove(req.params['id'])
    .then(function (deletedStudent) {
      sendInfo(errorCodes.Success, res, deletedStudent);
    })
    .catch(function (err) {
      if (err.code) {
        sendInfo(err.code, res, {});
      } else {
        handleErrors(err, res, {});
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
  Course.findById(courseId)
    .then(function (course) {
      // 课程不存在
      if (!course) {
        return Promise.reject({ code: errorCodes.CourseNotExist });
      }

      // 删除上传的文件
      promises.push(fs.unlink(file.path));
      
      // 修改课程信息
      course.set('academy', courseExtra.get('academy'));         
      course.set('studentCount', course.get('studentCount') + signStudents.length);
      promises.push(course.save());

      // 保存每一个导入的学生
      for (var i=0; i<signStudents.length; i++) {
        signStudents[i].set('teacherId', course.get('teacherId'));
        signStudents[i].set('courseId', courseId);
        signStudents[i].set('phone', '');     
        signStudents[i].set('createdAt', moment(new Date()).format('YYYY-MM-DD'));  
        promises.push(signStudents[i].save());
      }

      return Promise.all(promises);
    })
    .then(function (results) {
      sendInfo(errorCodes.Success, res, results.slice(2));
    })
    .catch(function (err) {
      if (err.code) {
        sendInfo(err.code, res, []);
      } else {
        handleErrors(err, res, []);
      }      
    }); 
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