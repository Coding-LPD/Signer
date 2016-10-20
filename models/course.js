var errorCodes = require('../services/error-codes').errorCodes;

var Course = {
  name: {
    type: String,
    required: errorCodes.CourseNameRequired
  },
  teacherId: {
    type: String,
    default: ''
  },
  location: {
    type: String,
    default: ''
  },
  academy: {
    type: String,
    default: ''
  },
  time: {
    type: String,
    default: ''
  },
  studentCount: {
    type: Number,
    default: 0
  },
  signCount: {
    type: Number,
    default: 0
  }
};

 module.exports = Course;