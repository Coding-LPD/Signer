var express = require('express');
var path = require('path');
var favicon = require('serve-favicon');
var logger = require('morgan');
var cookieParser = require('cookie-parser');
var bodyParser = require('body-parser');

var routes = require('./routes/index');
var publickey = require('./routes/publickey');
var users = require('./routes/users');
var smsCode = require('./routes/smsCode').router;
var student = require('./routes/student');
var teacher = require('./routes/teacher');
var signStudent = require('./routes/sign-student');
var course = require('./routes/course');
var sign = require('./routes/sign');
var signRecord = require('./routes/sign-record');

var app = express();

// view engine setup
app.set('views', path.join(__dirname, 'views'));
app.set('view engine', 'jade');

// uncomment after placing your favicon in /public
//app.use(favicon(path.join(__dirname, 'public', 'favicon.ico')));
app.use(logger('dev'));
app.use(bodyParser.json());
app.use(bodyParser.urlencoded({ extended: false }));
app.use(cookieParser());
app.use(express.static(path.join(__dirname, 'public')));
// 允许跨域访问
app.use(function(req, res, next) {
    res.setHeader('Access-Control-Allow-Origin', req.headers.origin || 'http://localhost:5000');
    res.setHeader('Access-Control-Allow-Credentials', true);
    res.setHeader('Access-Control-Allow-Methods', 'GET, POST, PUT, DELETE');
    res.setHeader('Access-Control-Allow-Headers', 'X-Requested-With,content-type');
    next();
});

app.use('/api', routes);
app.use('/api/publickey', publickey);
app.use('/api/users', users);
app.use('/api/smsCode', smsCode);
app.use('/api/students', student);
app.use('/api/teachers', teacher);
app.use('/api/signStudents', signStudent);
app.use('/api/courses', course);
app.use('/api/signs', sign);
app.use('/api/signRecords', signRecord);

// catch 404 and forward to error handler
app.use(function(req, res, next) {
  var err = new Error('Not Found');
  err.status = 404;
  next(err);
});

// error handlers

// development error handler
// will print stacktrace
if (app.get('env') === 'development') {
  app.use(function(err, req, res, next) {
    res.status(err.status || 500);
    res.render('error', {
      message: err.message,
      error: err
    });
  });
}

// production error handler
// no stacktraces leaked to user
app.use(function(err, req, res, next) {
  res.status(err.status || 500);
  res.render('error', {
    message: err.message,
    error: {}
  });
});

module.exports = app;
