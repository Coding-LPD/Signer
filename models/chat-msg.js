var mongoose = require('mongoose');

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
    required: true
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