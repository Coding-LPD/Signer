var errorCodes = require('../services/error-codes').errorCodes;
var common = require('../services/common');

var Sign = {
  courseId: {
    type: String,
    required: true
  },
  teacherId: {
    type: String,
    required: true
  },
  startTime: {
    type: String,
    default: ''
  },
  endTime: {
    type: String,
    default: ''
  },
  state: {
    type: Number,
    enum: {
      values: [0, 1, 2],
      message: errorCodes.SignStateError
    },
    required: true
  },
  beforeSignIn: {
    type: Number,
    default: 0
  },
  afterSignIn: {
    type: Number,
    default: 0
  },
  color: {
    type: String,
    default: ''
  },
  relatedId: {
    type: String,
    default: ''
  },
  code: {
    type: String,
    default: ''
  },
  createdAt: {
    type: String,
    default: ''
  },
  studentCount: {
    type: Number,
    default: 0
  },
  courseName: {
    type: String,
    default: ''    
  }
};

statics = {
  // 产生6位随机签到码
  generateSignCode: function () {
    var count = 6;
    var code = '';
    for (var i=0; i<count; i++) {
      code += common.getRandomNum(10);
    }
    return code;
  },

  getSignInName: function (type) {
    type = type || 0;
    return type == 0 ? 'beforeSignIn' : 'afterSignIn';
  }
}

methods = {

  getSignIn: function() {    
    return this.type == 0 ? this.get('beforeSignIn') : this.get('afterSignIn');
  }  
}


 exports.model = Sign;
 exports.statics = statics;
 exports.methods = methods;