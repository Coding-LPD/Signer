var path = require('path');
var xlsx = require('xlsx');
var moment = require('moment');
var express = require('express');
var router = express.Router();

var handleErrors = require('../services/error-handler').handleErrors;
var sendInfo = require('../services/error-handler').sendInfo; 
var errorCodes = require('../services/error-codes').errorCodes;
var common = require('../services/common');
var config = require('../config');
var Student = require('../services/mongo').Student;
var Teacher = require('../services/mongo').Teacher;
var SignStudent = require('../services/mongo').SignStudent;
var Course = require('../services/mongo').Course;
var Sign = require('../services/mongo').Sign;
var SignRecord = require('../services/mongo').SignRecord;
var Position = require('../services/mongo').Position;

router.get('/', function (req, res) {
  Sign.find(function (err, signs) {
    if (!err) {
      sendInfo(errorCodes.Success, res, signs);
    } else {
      handleErrors(err, res, []);
    }
  });
});

router.get('/:id', function (req, res){
  Sign.findById(req.params['id'], function (err, sign) {
    if (err) {
      handleErrors(err, res, {});
    } else {
      sendInfo(errorCodes.Success, res, sign);
    }
  });
}); 

router.post('/', function (req, res) {
  var courseId = req.body.courseId;
  var startTime = new Date(req.body.startTime);
  var endTime = new Date(req.body.endTime);
  var course;

  Promise.resolve()
    .then(function () {
      // 时间格式不对，返回错误信息 
      if (isNaN(startTime.valueOf()) || isNaN(endTime.valueOf())) {
        return Promise.reject({ code: errorCodes.TimeInvalid });
      }

      // 起始时间晚于结束时间
      if (moment(endTime).isBefore(startTime)) {
        return Promise.reject({ code: errorCodes.TimeRangeError });        
      }

      // 查询课程
      return Course.findById(courseId);
    })    
    .then(function (findedCourse) {
      // 课程不存在
      if (!findedCourse) {
        return Promise.reject({ code: errorCodes.CourseNotExist });
      }
      course = findedCourse;

      // 生成签到码
      return generateDistinctSignCode();
    })
    .then(function (code) { 
      // 判断签到状态
      var state = 0, now = moment(new Date());
      if (now.isBefore(startTime)) {
        state = 0;
      } else if (now.isAfter(endTime)) {
        state = 2;
      } else {
        state = 1;
      }
      var createdAt = now.format('YYYY-MM-DD HH:mm');  

      // 创建签到信息
      var newSign = new Sign(req.body);    
      newSign.set('courseName', course.get('name'));
      newSign.set('teacherId', course.get('teacherId'));
      newSign.set('createdAt', createdAt);
      newSign.set('state', state);
      newSign.set('code', code);
      
      var promises = [];
      // 保存签到信息
      promises.push(newSign.save());
      // 修改课程签到次数
      promises.push(Course.findByIdAndUpdate(course._id, { $inc: { signCount: 1 } }));
      return Promise.all(promises);
    })
    .then(function (results) {
      sendInfo(errorCodes.Success, res, results[0]);
    }) 
    .catch(function (err) {
      if (err.code) {
        sendInfo(err.code, res, []);
      } else {
        handleErrors(err, res, []);
      }      
    });
});

router.put('/:id', function (req, res) {
  var signId = req.params['id'];
  var isAfterOpen = req.body.isAfterOpen || false;

  Sign.findByIdAndUpdate(signId, { isAfterOpen: isAfterOpen }, function (err, updatedSign) {
    if (err) {
      handleErrors(err, res, {});
    } else {
      sendInfo(errorCodes.Success, res, updatedSign);
    }
  });
})

router.delete('/:id', function (req, res) {
  var signId = req.params['id'];
  var promises = [];
  var sign;

  // 删除签到
  Sign.findByIdAndRemove(req.params['id'])
    .then(function (deletedSign) {
      sign = deletedSign;

      // 删除签到相关记录和定位信息，并将课程签到次数减1
      promises.push(SignRecord.remove({ signId: signId }));
      promises.push(Position.remove({ signId: signId }));
      promises.push(Course.findByIdAndUpdate(sign.get('courseId'), { $inc: { signCount: -1 } }));      

      return Promise.all(promises);
    })
    .then(function () {
      sendInfo(errorCodes.Success, res, sign);
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
  var sortby = req.query['sortby'] || '';
  var order = req.query['order'] || 1;
  var options = null;
  if (sortby) {
    var sort = (order == 1 ? '' : '-') + sortby;
    options = { sort: sort };
  }
  // 必须要有查询条件
  if (common.isEmptyObject(req.body)) {
    sendInfo(errorCodes.SearchEmpty, res, []);
    return;
  }
  var conditions = req.body;
  if (conditions.courseName) {
    conditions.courseName = new RegExp(common.replaceRegSpecial(conditions.courseName));
  }
  Sign.find(conditions, null, options, function (err, signs) {
    if (!err) {
      sendInfo(errorCodes.Success, res, signs);
    } else {
      handleErrors(err, res, []);
    }
  });
});

router.get('/scanning/:code', function (req, res) {
  var maxRecordNum = 10;
  Sign.find({code: req.params['code']}, function (err, signs) {
    if (err) {
      handleErrors(err, res, {});
      return;
    }
    if (signs.length == 0) {
      sendInfo(errorCodes.SignNotExist, res, {});
      return;
    }
    var sign = signs[0];
    
    var promises = [];
    promises.push(Course.findById(sign.get('courseId'), 'name time location'));
    promises.push(Teacher.findById(sign.get('teacherId'), 'name'));
    promises.push(SignRecord.find({signId: sign._id}, 'studentName studentAvatar', { limit: maxRecordNum, sort: { createdAt: -1 } }));
    Promise.all(promises).then(function (findedData) {
      var signId = sign._id;
      var course = findedData[0].toObject();
      course.teacherName = findedData[1].get('name');
      // 格式化返回值的属性名
      var records = [];
      findedData[2].forEach(function (value, index) {
        records[index] = {
          _id: value._id,
          name: value.get('studentName'),
          avatar: value.get('studentAvatar')
        };
      });
      if (!course) {
        sendInfo(errorCodes.SignNotRelatedCourse, res, {});
        return;
      }
      sendInfo(errorCodes.Success, res, { course, records, signId });            
    }).catch(function (err) {
      handleErrors(err, res, {});
    })
  });
});

router.post('/export', function (req, res) {
  var courseId = req.body.courseId;
  var signId = req.body.signId;
  var type = req.body.type;
  var signStudents, signRecords;
  var promises = [];
  
  // 查询课程导入的相应的学生数据，以及某次签到的所有课前记录
  promises.push(SignStudent.find({ courseId: courseId }));
  promises.push(SignRecord.find({ courseId: courseId, signId: signId, type: type }))
  Promise.all(promises)
    .then(function (results) {
      signStudents = results[0];
      signRecords = results[1];

      // 查询每个记录中学生对应的学号
      return Promise.all(signRecords.map(function (record) {
        return Student.findById(record.get('studentId'));
      }));
    })
    .then(function (results) {
      // 将记录转换为纯粹js对象，并添加学生对应的学号
      signRecords = signRecords.map(function (record, index) {
        var obj = record.toObject();
        obj.number = results[index].get('number');
        return obj;
      });
      // 将课程导入的学生转换为纯粹js对象，并添加是否签到的属性
      signStudents = signStudents.map(function (student) {
        var obj = student.toObject();
        obj.isSign = false;
        return obj;
      });
      // 学号从小到大排序
      signRecords.sort(function (a, b) { return a.number - b.number; });
      signStudents.sort(function (a, b) { return a.number - b.number; });
      // 查询学生对应的记录，有记录且教师批准，说明完成签到，否则表示没签到      
      for (var i=0; i<signStudents.length; i++) {
        for (var j=0; j<signRecords.length; j++) {
          if (signStudents[i].number == signRecords[j].number) {
            signStudents[i].isSign = signRecords[j].state == 1 ? true : false;
            signRecords.shift();
            break;
          } else if (signStudents[i].number < signRecords[j].number) {
            break;
          }
        }
      }
      // 将签到情况记录为excel，并提供给客户端下载
      var savePath = path.resolve(__dirname, '../public/temp');
      var workbook = getSignWorkbook(signStudents);
      var timestamp = new Date().getTime()
      var fileName = timestamp + '.xlsx';
      savePath += '/' + fileName;
      xlsx.writeFile(workbook, savePath);
      sendInfo(errorCodes.Success, res, config.fileDownload + fileName);
    })
    .catch(function (err) {
      if (err.code) {
        sendInfo(err.code, res, '');
      } else {
        handleErrors(err, res, '');
      }
    });
})

// 检查数据库中是否已有相同签到码，有则重新生成并检查
function generateDistinctSignCode() {
  var code = Sign.generateSignCode();
  
  return Sign.find({ code: code })
    .then(function (signs) {
      if (signs.length > 0) {
        return generateDistinctSignCode().then(function (code) {
          return code;
        });
      } else {
        return code;
      }
    })
}

function getSignWorkbook(signStudents) {    
  var filePath = path.resolve(__dirname, '../public/template.xlsx');
  var workbook = xlsx.readFile(filePath);  
  var worksheet = workbook.Sheets[workbook.SheetNames[0]];
  var row = 2;
  // 构造excel表单
  worksheet['A1'] = { v: '学号' };
  worksheet['B1'] = { v: '姓名' };
  worksheet['C1'] = { v: '签到情况' };
  for (var i=0; i<signStudents.length; i++) {
    worksheet['A' + row] = { v: signStudents[i].number };
    worksheet['B' + row] = { v: signStudents[i].name };
    worksheet['C' + row] = { v: signStudents[i].isSign ? '已签' : '' };
    row++;
  }
  worksheet['!ref'] = 'A1:' + 'C' + (row-1);
  // 覆盖原有excel表单
  workbook.Sheets[workbook.SheetNames[0]] = worksheet;
  return workbook;
}

module.exports = router;