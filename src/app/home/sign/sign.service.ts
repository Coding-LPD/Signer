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

  search(condition: Object, sortby?: string, order?: number) {
    var url = `${API.domain}${API.signSearch}?sortby=${sortby}&order=${order}`;
    return this._http.post(url, condition, this.options)
      .map(this.extractData)
      .catch(this.handleError);
  }

  openAfterSign(signId: string) {
    return this._http.put(API.domain + API.sign + `/${signId}`, { isAfterOpen: true })
      .map(this.extractData)
      .catch(this.handleError);
  }

}