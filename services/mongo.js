var mongoose = require('mongoose');
var Schema = mongoose.Schema;
var config = require('../config');
var log = require('./log');
var jwtService = require('./jwt-service');

var user = require('../models/user');
var student = require('../models/student');
var teacher = require('../models/teacher');
var course = require('../models/course');
var sign = require('../models/sign');
var signStudent = require('../models/sign-student');
var signRecord = require('../models/sign-record');
var position = require('../models/position');

mongoose.connect(config.connection);
mongoose.set('debug', config.debug);

var db = mongoose.connection;
db.on('error', function (err) {
  log.error(err.message);
});

var UserSchema = mongoose.Schema(user);
var StudentSchema = mongoose.Schema(student); 
var TeacherSchema = mongoose.Schema(teacher);
var CourseSchema = mongoose.Schema(course);
var SignSchema = mongoose.Schema(sign.model);
var SignStudentSchema = mongoose.Schema(signStudent);
var SignRecordSchema = mongoose.Schema(signRecord);
var PositionSchema = mongoose.Schema(position);

SignSchema.statics = sign.statics;

var User = mongoose.model('user', UserSchema);
var Student = mongoose.model('student', StudentSchema);
var Teacher = mongoose.model('teacher', TeacherSchema);
var Course = mongoose.model('course', CourseSchema);
var Sign = mongoose.model('sign', SignSchema);
var SignStudent = mongoose.model('signStudent', SignStudentSchema);
var SignRecord = mongoose.model('signRecord', SignRecordSchema);
var Position = mongoose.model('position', PositionSchema);

exports.User = User;
exports.Student = Student;
exports.Teacher = Teacher;
exports.Course = Course;
exports.Sign = Sign;
exports.SignStudent = SignStudent;
exports.SignRecord = SignRecord;
exports.Position = Position;