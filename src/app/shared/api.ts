export class API {

  // static domain = 'http://linkdust.xicp.net:50843/api';
  static domain = 'http://localhost:3000/api';

  static publickey = '/publickey';
  static smsCode = '/smsCode';
  static smsCodeVerification = '/smsCode/verification';
  static smsCodeState = '/smsCode/state';
  static loginWithPassword = '/users/login';
  static loginWithSmsCode = '/users/loginWithSmsCode';
  static user = '/users';
  static userSearch = '/users/search';
  static student = '/signStudents';
  static studentSearch = '/signStudents/search';
  static studentImport = '/signStudents/import';
  static teacher = '/teachers';
  static teacherSearch = '/teachers/search';
  static course = '/courses';
  static courseSearch = '/courses/search';
  static sign = '/signs';
  static signSearch = '/signs/search';

}