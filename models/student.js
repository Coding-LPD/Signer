var errorCodes = require('../services/error-codes').errorCodes;

var Student = {
  phone: { 
    type: String, 
    required: errorCodes.PhoneRequired 
  },
  number: {
    type: String,
    default: ''
  },
  name: {
    type: String,
    default: '学生'
  },
  gender: {
    type: String,
    default: ''
  },
  school: {
    type: String,
    default: ''
  },
  academy: {
    type: String,
    default: ''
  },
  major: {
    type: String,
    default: ''
  },
  grade: {
    type: String,
    default: ''
  },
  _class: {
    type: String,
    default: ''
  },
  mail: {
    type: String,
    default: ''
  },
  avatar: {
    type: String,
    default: ''
  }
};

 module.exports = Student;