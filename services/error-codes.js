var errorCodes = {
  // 错误码
  PhoneRequired:          '1000',
  PasswordRequired:       '1001',  
  RoleRequired:           '1002',
  RoleEnum:               '1003',
  PhoneError:             '1004',
  PasswordDecryptError:   '1005',
  StudentNumberRequired:  '1006',
  CourseNameRequired:     '1007',  
  SearchEmpty:            '1008',
  CourseStateError:       '1009',
  CourseStartTimeEmpty:   '1010',
  CourseEndTimeEmpty:     '1011',
  CourseStartTimeInvalid: '1012',
  CourseEndTimeInvalid:   '1013',
  CourseTimeRangeError:   '1014',
  NoFileError:            '1015',
  NoDefaultImageError:    '1016',

  AuthInfoEmpty:          '2000',
  AuthInfoError:          '2001',
  UserExist:              '2002',
  UserNotExist:           '2003',

  SmsCodeError:           '3000',
  SmsCodeNotVerify:       '3001',
  SmsCodeLimit:           '3002',
  BmobOtherError:         '3003',
  SmsidError:             '3004',
  
  AuthenticationError:    '401',
  AuthenticationExpired:  '402',
  NoAuthorization:        '403',
  OtherError:             '500',
  // 成功
  Success:                '200',
}

var errorMsg = {
  1000: '手机号码不能为空',
  1001: '密码不能为空',
  1002: '角色不能为空',
  1003: '用户角色的值只能为"0"、"1"',
  1004: '手机号码无效',
  1005: '密码解密失败',
  1006: '学号不能为空',
  1007: '课程名称不能为空',
  1008: '查询条件不能为空',
  1009: '课程状态的值只能为0、1、2',
  1010: '课程起始时间不能为空',
  1011: '课程结束时间不能为空',
  1012: '课程起始时间格式不正确',
  1013: '课程结束时间格式不正确',
  1014: '课程开始时间要在结束时间之前',
  1015: '服务器没接收到上传的文件',
  1016: '不存在该默认图片',
  2000: '手机号码或密码不能为空',
  2001: '手机号码或密码错误',
  2002: '该手机号码已被注册',
  2003: '用户不存在',
  3000: '验证码错误或者已被验证', 
  3001: '该验证码尚未被验证',
  3002: '该号码已达到短信验证码发送上限',
  3004: '无效的smsid',  
  401:  '认证信息有误',
  402:  '认证信息已失效',
  403:  '无权限访问该资源',
  500:  '内部服务器错误',  
  200:  '操作成功'
}

exports.errorCodes = errorCodes;
exports.errorMsg = errorMsg;