var errorCodes = require('../services/error-codes').errorCodes;

var SignStudent = {
  name: {
    type: String
  },
  number: {
    type: String,
    required: errorCodes.StudentNumberRequired
  },
  phone: {
    type: String,
  },
  major: {
    type: String
  },
  teacherId: {
    type: String,
    required: true
  },
  courseId: {
    type: String,
    required: true
  },
  createdAt: {
    type: String
  }
};

 module.exports = SignStudent;