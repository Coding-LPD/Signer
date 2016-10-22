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
    var data = {
      phone,
      password: this._jsEncryptService.encrypt(password)
    };
    return this._http.post(API.domain + API.loginWithPassword, data, { withCredentials: true })
        .map(res => {
          var body = this.extractData(res);
          if (body.code && +body.code == 200) {
            this.isLoggedIn = true;
            this.user = body.data.user;
          }
          return body;
        })
        .catch(this.handleError)
  }

  loginWithSmsCode(phone: string, smsCode: string) {
    var data = { phone, smsCode };
    return this._http.post(API.domain + API.loginWithSmsCode, data, { withCredentials: true})
      .map(res => {
        var body = this.extractData(res);
        if (body.code && +body.code == 200) {
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
    return new Observable<any>((observer: Subscriber<any>) => {
      this._http.post(API.domain + API.teacherSearch, { phone: this.user.phone }, this.options)
        .map(this.extractData)
        .catch(this.handleError)
        .subscribe(body => {
          if (+body.code == 200) {
            this.teacher = body.data[0];
          }
          observer.next(body);
        });
    });
  }

  getCourses(refresh: boolean = false): Observable<any> {
    if (this.courses && !refresh) {
      return Observable.of<any>({
        code: '200',
        data: this.courses
      });
    } 
    return new Observable<any>((observer: Subscriber<any>) => {
      this.getTeacherInfo().subscribe(body => {
        if (+body.code == 200) {
          var teacherId = body.data[0]._id;
          this._http.post(API.domain + API.courseSearch, { teacherId })
            .map(this.extractData)
            .catch(this.handleError)
            .subscribe(body => {
              if (+body.code == 200) {
                this.courses = body.data;
              } 
              observer.next(body);
            });
        } else {
          observer.next(body);
        }
      })
    });     
  }

  logout() {
    this.user = null;
    this.teacher = null;
    this.courses = null;
    this.isLoggedIn = false;
  }

}