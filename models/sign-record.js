var errorCodes = require('../services/error-codes').errorCodes;

var SignRecord = {
  signId: {
    type: String,
    required: true
  },
  phoneId: {
    type: String,
    required: true
  },
  studentId: {
    type: String,
    required: true
  },
  studentName: {
    type: String
  },
  studentAvatar: {
    type: String
  },
  distance: {
    type: Number
  },
  state: {
    type: Number,
    enum: {
      values: [0, 1, 2],
      message: errorCodes.SignRecordStateError
    }
  },
  type: {
    type: Number,
    enum: {
      values: [0, 1],
      message: errorCodes.SignRecordTypeError
    }
  },
  createdAt: {
    type: String
  },
  battery: {
    type: Number
  }
};

 module.exports = SignRecord;