var errorCodes = require('../services/error-codes').errorCodes;
var config = require('../config');

var Teacher = {
  phone: { 
    type: String, 
    required: errorCodes.PhoneRequired 
  },
  name: {
    type: String,
    default: '教师'    
  },
  school: {
    type: String,
    default: ''
  },
  academy: {
    type: String,
    default: ''
  },
  avatar: {
    type: String,
    default: config.userImagesUrlPrefix + '1.png'
  }
};

 module.exports = Teacher;