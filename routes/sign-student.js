var express = require('express');
var fs = require('fs');
var path = require('path');
var xlsx = require('xlsx');
var multipart = require('connect-multiparty');
var moment = require('moment');
var router = express.Router();
var multipartMiddleware = multipart();

var handleErrors = require('../services/error-handler').handleErrors;
var sendInfo = require('../services/error-handler').sendInfo;
var errorCodes = require('../services/error-codes').errorCodes;
var common = require('../services/common');
var config = require('../config');
var Student = require('../services/mongo').Student;
var Course = require('../services/mongo').Course;
var SignStudent = require('../services/mongo').SignStudent;
var Sign = require('../services/mongo').Sign;
var SignRecord = require('../services/mongo').SignRecord;

router.get('/', function (req, res) {
  SignStudent.find(function (err, students) {
    if (err) {
      handleErrors(err, res, []);      
    } else {
      sendInfo(errorCodes.Success, res, students);
    }
  });
});

router.post('/', function (req, res) {
  var courseId = req.body.courseId;  

  Promise.resolve()
    .then(function () {
      if (common.isEmptyString(req.body.name)) {
        return Promise.reject({ code: errorCodes.SignStudentNameRequired });
      }
      if (common.isEmptyString(req.body.number)) {
        return Promise.reject({ code: errorCodes.SignStudentNumberRequired });
      }

      return Course.findById(courseId);
    })  
    .then(function (findedCourse) {
      // 课程不存在
      if (!findedCourse) {
        return Promise.reject({ code: errorCodes.CourseNotExist });
      }

      return SignStudent.findOne({ courseId: courseId, number: req.body.number });
    })
    .then(function (findedStudent) {
      // 课程中已经有对应学号的学生
      if (findedStudent) {
        return Promise.reject({ code: errorCodes.SignStudentSameNumber });
      }

      var promises = [];
      var course = findedCourse;

      // 保存新学生信息
      var signStudent = new SignStudent(req.body);
      signStudent.set('teacherId', course.get('teacherId'));
      signStudent.set('createdAt', moment().format('YYYY-MM-DD'));
      promises.push(signStudent.save());

      // 修改课程信息
      promises.push(Course.findByIdAndUpdate(courseId, { $inc: { studentCount: 1 } }, { new: true }));

      // 修改课程相关签到的学生数量，以及使用该课程的学生表的签到的学生数量
      promises.push(Sign.update({ $or: [ { courseId: courseId }, { relatedId: courseId } ] }, { $inc: { studentCount: 1 } }, { multi: true }));

      return Promise.all(promises);
    })
    .then(function (results) {
      sendInfo(errorCodes.Success, res, results[0]);
    })
    .catch(function (err) {
      if (err.code) {
        sendInfo(err.code, res, {});
      } else {
        handleErrors(err, res, {});
      }      
    });  
});

router.put('/:id', function (req, res) {
  var studentId = req.params['id'];

  delete req.body._id;
  delete req.body.courseId;
  delete req.body.teacherId;
  delete req.body.createdAt;

  SignStudent.findByIdAndUpdate(studentId, req.body, function (err, savedStudent) {
    if (err) {
      handleErrors(err, res, {});
    } else {
      sendInfo(errorCodes.Success, res, savedStudent);
    }
  });
});

router.delete('/:id', function (req, res) {
  var student;
  var promises = [];

  SignStudent.findByIdAndRemove(req.params['id'])
    .then(function (deletedStudent) {
      student = deletedStudent;

      // 课程的学生数量减1
      return Course.findByIdAndUpdate(student.get('courseId'), { $inc: { studentCount: -1 } }, { new: true });
    })
    .then(function (updatedCourse) {
      // 课程相关的签到的学生数量都要同步
      return Sign.update({ $or: [ { courseId: updatedCourse._id }, { relatedId: updatedCourse._id } ] }, { studentCount: updatedCourse.get('studentCount') }, { multi: true });
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

router.post('/mutil/remove', function (req, res) {
  var courseId = req.body.courseId;
  var ids = req.body.ids;

  SignStudent.remove({ _id: { $in: ids } })
    .then(function (result) {
      // 删除成功的数目
      var successNum = result.result.n;

      // 课程学生数量减少相应数目
      return Course.findByIdAndUpdate(courseId, { $inc: { studentCount: -successNum } }, { new: true });
    })
    .then(function (updatedCourse) {
      // 课程相关的签到的学生数量都要同步
      return Sign.update({ $or: [ { courseId: updatedCourse._id }, { relatedId: updatedCourse._id } ] }, { studentCount: updatedCourse.get('studentCount') }, { multi: true });
    })
    .then(function (updatedSigns) {
      sendInfo(errorCodes.Success, res);
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
  var file = req.files.fieldNameHere || req.files.file;
  var promises = [];
  var courseExtra, signStudents;    

  Promise.resolve()
    .then(function () {            
      var workbook = xlsx.readFile(file.path);
      var worksheet = workbook.Sheets[workbook.SheetNames[0]];  
      courseExtra = readHeader(worksheet);
      signStudents = readBody(worksheet);

      return Course.findById(courseId);
    })
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

      // 修改课程相关签到的学生数量，以及使用该课程的学生表的签到的学生数量
      promises.push(Sign.update({ $or: [ { courseId: course._id }, { relatedId: course._id } ] }, { studentCount: studentCount }, { multi: true }));      

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

router.post('/export', function (req, res) {
  var courseId = req.body.courseId;

  SignStudent.find({ courseId: courseId }, null, { sort: '-number' })
    .then(function (signStudents) {      
      // 将签到情况记录为excel，并提供给客户端下载
      var savePath = path.resolve(__dirname, '../public/temp');
      var workbook = getSignSutdentWorkbook(signStudents);
      var timestamp = new Date().getTime()
      var fileName = timestamp + '.xlsx';
      savePath += '/' + fileName;
      xlsx.writeFile(workbook, savePath);
      sendInfo(errorCodes.Success, res,  config.fileDownload + fileName);
    })
    .catch(function (err) {
      if (err.code) {
        sendInfo(err.code, res, '');
      } else {
        handleErrors(err, res, '');
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

function getSignSutdentWorkbook(signStudents) {
  var filePath = path.resolve(__dirname, '../public/template.xlsx');
  var workbook = xlsx.readFile(filePath);  
  var worksheet = workbook.Sheets[workbook.SheetNames[0]];
  var row = 2, phone;
  // 构造excel表单
  worksheet['A1'] = { v: '学号' };
  worksheet['B1'] = { v: '姓名' };
  worksheet['C1'] = { v: '专业' };
  worksheet['D1'] = { v: '联系方式' };
  for (var i=0; i<signStudents.length; i++) {
    phone = signStudents[i].get('phone')
    worksheet['A' + row] = { v: signStudents[i].get('number') };
    worksheet['B' + row] = { v: signStudents[i].get('name') };
    worksheet['C' + row] = { v: signStudents[i].get('major') };
    worksheet['D' + row] = { v: phone ? phone : '暂无' };
    row++;
  }
  worksheet['!ref'] = 'A1:' + 'D' + (row-1);
  // 覆盖原有excel表单
  workbook.Sheets[workbook.SheetNames[0]] = worksheet;
  return workbook;
}

module.exports = router;