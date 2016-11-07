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
var Sign = require('../services/mongo').Sign;

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
  var student,  course;
  var promises = [];

  SignStudent.findByIdAndRemove(req.params['id'])
    .then(function (deletedStudent) {      
      student = deletedStudent;

      // 课程的学生数量减1
      return Course.findByIdAndUpdate(student.get('courseId'), { $inc: { studentCount: -1 } }, { new: true });      
    })
    .then(function (updatedCourse) {
      course = updatedCourse;

      // 课程相关的签到的学生数量都要同步
      return Sign.update({ courseId: updatedCourse._id }, { studentCount: course.get('studentCount') }, { multi: true });
    })
    .then(function (updatedSigns) {            
      sendInfo(errorCodes.Success, res, student);
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
      var studentCount = course.get('studentCount') + signStudents.length;
      course.set('academy', courseExtra.get('academy'));         
      course.set('studentCount', studentCount);
      promises.push(course.save());

      // 修改课程相关签到的学生数量
      promises.push(Sign.update({ courseId: course._id }, { studentCount: studentCount }, { multi: true }));

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
      sendInfo(errorCodes.Success, res, results.slice(3));
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