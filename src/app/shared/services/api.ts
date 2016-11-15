export class API {

  // ip service
  // static ipService = 'https://jsonip.com';
  static ipService = 'http://120.25.65.207:3000/api/positions/ip';

  // static domain = 'http://linkdust.xicp.net:50843/api';
	static domain = 'http://localhost:3000/api';
	// static domain = 'http://120.25.65.207:3000/api';

  static publickey = '/publickey';
  static smsCode = '/smsCode';
  static smsCodeVerification = '/smsCode/verification';
  static smsCodeState = '/smsCode/state';
  static loginWithPassword = '/users/login';
  static loginWithSmsCode = '/users/loginWithSmsCode';
  static user = '/users';
  static userSearch = '/users/search';
  static studentRelatedInfo = '/students/relatedInfo';
  static signStudent = '/signStudents';
  static signStudentSearch = '/signStudents/search';
  static signStudentImport = '/signStudents/import';
  static teacher = '/teachers';
  static teacherSearch = '/teachers/search';
  static course = '/courses';
  static courseSearch = '/courses/search';
  static courseLatestStatistics = '/courses/{0}/statistics/latest';
  static courseAllStatistics = '/courses/{0}/statistics/all';
  static sign = '/signs';
  static signSearch = '/signs/search';
  static signRecord = '/signRecords';
  static signRecordAssent = '/signRecords/{0}/assent';
  static signRecordAssentAll = '/signRecords/assent/all'  
  static signRecordRefusal = '/signRecords/{0}/refusal';  
  static signRecordRefusalAll = '/signRecords/refusal/all'
  static signRecordSearch = '/signRecords/search';
  static signRecordAddition = '/signRecords/addition';
  static position = '/positions';

  static stringReplace(str: string, params: string[]) {
      var i: number;
      var reg: RegExp;
      
      for (i=0; i<params.length; i++) {        
          reg = new RegExp('\\{' + i + '\\}', 'g');
          str = str.replace(reg, params[i]);
      }
      return str;
  }

}