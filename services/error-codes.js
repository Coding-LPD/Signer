var errorCodes = {
  // 错误码
  PhoneRequired:          '1000',
  PasswordRequired:       '1001',  
  RoleRequired:           '1002',
  RoleError:              '1003',
  PhoneError:             '1004',
  PasswordDecryptError:   '1005',
  StudentNumberRequired:  '1006',
  CourseNameRequired:     '1007',  
  SearchEmpty:            '1008',
  SignStateError:          '1009',  
  NoFileError:            '1015',
  NoDefaultImageError:    '1016',
  SignRecordStateError:   '1017',
  SignRecordTypeError:    '1018',

  AuthInfoEmpty:          '2000',
  AuthInfoError:          '2001',
  UserExist:              '2002',
  UserNotExist:           '2003',

  SmsCodeError:           '3000',
  SmsCodeNotVerify:       '3001',
  SmsCodeLimit:           '3002',
  BmobOtherError:         '3003',
  SmsidError:             '3004',

  SignNotExist:           '4000',
  SignNotRelatedCourse:   '4001',
  IPEmpty:                '4002',
  LocateError:            '4003',
  TeacherNotLocate:       '4004',
  
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
  1009: '签到类型的值只能为0、1、2',
  1015: '服务器没接收到上传的文件',
  1016: '不存在该默认图片',
  1017: '签到记录的状态的值只能为0、1、2',
  1018: '签到记录的类型的值只能为0、1',
  2000: '手机号码或密码不能为空',
  2001: '手机号码或密码错误',
  2002: '该手机号码已被注册',
  2003: '用户不存在',
  3000: '验证码错误或者已被验证', 
  3001: '该验证码尚未被验证',
  3002: '该号码已达到短信验证码发送上限',
  3004: '无效的smsid',  
  4000: '签到失败，该签到不存在',
  4001: '签到失败，该签到没有对应某个课程',
  4002: 'IP地址不能为空',
  4003: '定位失败',
  4004: '教师尚未定位，无法进行签到',
  401:  '认证信息有误',
  402:  '认证信息已失效',
  403:  '无权限访问该资源',
  500:  '内部服务器错误',  
  200:  '操作成功'
}

exports.errorCodes = errorCodes;
exports.errorMsg = errorMsg;