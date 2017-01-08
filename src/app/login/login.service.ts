import { Injectable } from '@angular/core';
import { Http } from '@angular/http';
import { Observable } from 'rxjs/Observable';
import { Subscriber } from 'rxjs/Subscriber';

import { BaseService, API, JsEncryptService, User, Teacher, Course } from '../shared';

@Injectable()
export class LoginService extends BaseService {

  user: User;
  private teacher: Teacher;
  private courses: Course[];
  isLoggedIn = false;
  redirectUrl: string;

  constructor(
    private _http: Http,
    private _jsEncryptService: JsEncryptService) {
      super();
  }

  loginWithPassword(phone: string, password: string) {
    return this._jsEncryptService.encrypt(password)
      .flatMap(body => {
        if (+body.code == 200) {
          var data = { phone, password: body.data };
          return this._http.post(API.domain + API.loginWithPassword, data, this.options)
                           .map(this.extractData);
        } else {
          return Observable.of(body);
        }
      })
      .map(body => {
        if (+body.code == 200) {
          this.isLoggedIn = true;
          this.user = body.data.user;
        }
        return body;
      })
      .catch(this.handleError);
  }

  loginWithSmsCode(phone: string, smsCode: string) {
    var data = { phone, smsCode };
    return this._http.post(API.domain + API.loginWithSmsCode, data, { withCredentials: true})
      .map(this.extractData)
      .map(body => {
        if (+body.code == 200) {
          this.isLoggedIn = true;
          this.user = body.data.user;
        }
        return body;
      })
      .catch(this.handleError);
  }

  getTeacherInfo(refresh: boolean = false): Observable<any> {
    if (this.teacher && !refresh) {
      return Observable.of<any>({
        code: '200',
        data: [this.teacher]
      });
    }
    return this._http.post(API.domain + API.teacherSearch, { phone: this.user.phone }, this.options)      
      .map(this.extractData)
      .map(body => {
        if (+body.code == 200 && body.data.length > 0) {
          this.teacher = body.data[0];
        }
        return body;
      })
      .catch(this.handleError)
  }

  getCourses(refresh: boolean = false): Observable<any> {
    if (this.courses && !refresh) {
      return Observable.of<any>({
        code: '200',
        data: this.courses
      });
    } 
    return this.getTeacherInfo()
      .map(body => {        
        if (+body.code == 200 && body.data.length > 0) {          
          return body.data[0]._id;
        } else {
          return null;
        }
      })
      .flatMap(teacherId => {        
        if (teacherId) {
          return this._http.post(API.domain + API.courseSearch, { teacherId })
            .map(this.extractData)
        } else {
          return Observable.of<any>({
            code: '600',
            msg: '获取教师信息失败'            
          });
        }
      })
      .catch(this.handleError);
  }

  logout() {
    this.user = null;
    this.teacher = null;
    this.courses = null;
    this.isLoggedIn = false;
  }

}