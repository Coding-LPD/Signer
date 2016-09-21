import { Injectable } from '@angular/core';
import { Http } from '@angular/http';

import { BaseService, Sign, Course, API } from '../../shared'; 

@Injectable()
export class SignService extends BaseService {

  // 签到数据
  sign = new Sign();
  // 签到所属课程
  course: Course;
  // 关联的学生表
  relatedCourse: Course;

  constructor(private _http: Http) {
    super();
  }

  selectCourse(course: Course) {
    this.course = course;
    this.sign.courseId = course._id;
    if (!this.relatedCourse) {
      this.selectRelatedCourse(course);
    }
  }

  selectRelatedCourse(course: Course) {
    this.relatedCourse = course;
    this.sign.relatedId = course._id;
    this.sign.studentCount = course.studentCount;
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

  search(teacherId: string) {
    return this._http.post(API.domain + API.signSearch, {teacherId}, this.options)
      .map(this.extractData)
      .catch(this.handleError);
  }

}