var errorCodes = require('../services/error-codes').errorCodes;

var User = {
  phone: { 
    type: String, 
    required: errorCodes.PhoneRequired 
  },
  password: { 
    type: String, 
    required: errorCodes.PasswordRequired 
  },
  role: { 
    type: String,
    required: errorCodes.RoleRequired,
    enum: {
      values: ['0', '1'],
      message: errorCodes.RoleError
    }
  }
};

 module.exports = User;