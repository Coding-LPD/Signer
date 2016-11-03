import { Injectable } from '@angular/core';
import { Http } from '@angular/http';

import { BaseService, Sign, Course, API } from '../../shared'; 

@Injectable()
export class SignService extends BaseService {

  // 签到数据
  sign = new Sign();

  constructor(private _http: Http) {
    super();
  }

  selectCourse(course: Course) {
    this.sign.courseId = course._id || '';
    this.sign.courseName = course.name || '';
    if (!this.sign.relatedId) {
      this.selectRelatedCourse(course);
    }
  }

  selectRelatedCourse(course: Course) {
    this.sign.relatedId = course._id || '';
    this.sign.studentCount = course.studentCount || 0;
  }

  create(sign: Sign) {
    return this._http.post(API.domain + API.sign, sign, this.options)
      .map(this.extractData)
      .catch(this.handleError);
  }

  remove(id: string) {
    return this._http.delete(API.domain + API.sign + `/${id}`, this.options)
      .map(this.extractData)
      .catch(this.handleError);
  }

  search(condition: Object) {
    return this._http.post(API.domain + API.signSearch, condition, this.options)
      .map(this.extractData)
      .catch(this.handleError);
  }

}