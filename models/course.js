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
  startTime: {
    type: String,
    required: errorCodes.CourseStartTimeEmpty
  },
  endTime: {
    type: String,
    required: errorCodes.CourseEndTimeEmpty
  },
  studentCount: {
    type: Number,
    default: 0
  },
  signCount: {
    type: Number,
    default: 0
  },
  state: {
    type: Number,
    enum: {
      values: [0, 1, 2],
      message: errorCodes.CourseStateError
    }
  }
};

 module.exports = Course;