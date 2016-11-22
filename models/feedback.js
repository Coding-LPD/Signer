var errorCodes = require('../services/error-codes').errorCodes;

var Feedback = {
  studentId: {
    type: String,
    default: ''    
  },
  teacherId: {
    type: String,
    default: ''
  },
  name: {
    type: String,
    default: ''
  },
  phone: {
    type: String,
    default: ''
  },
  content: {
    type: String,
    required: errorCodes.RoleRequired,
  },
  createdAt: {
    type: String,
    default: ''
  }
};