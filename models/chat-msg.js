var mongoose = require('mongoose');
var errorCodes = require('../services/error-codes').errorCodes;

var ChatMsg = {
  courseId: {
    type: String,
    required: true
  },
  teacherId: {
    type: String,
    default: ''
  },
  studentId: {
    type: String,
    default: ''
  },
  content: {
    type: String,
    required: errorCodes.MsgContentIsEmpty
  },
  avatar: {
    type: String,
    default: ''
  },
  name: {
    type: String,
    default: ''
  },
  createdAt: {
    type: String,
    required: true
  }
};

module.exports = ChatMsg;