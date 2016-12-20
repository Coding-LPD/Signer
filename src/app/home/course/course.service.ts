import { Injectable } from '@angular/core';
import { Http } from '@angular/http';

import { BaseService, API, Course } from '../../shared';

@Injectable()
export class CourseService extends BaseService {

  constructor(private _http: Http) {
    super();
  }

  getCourse() {
    return this._http.get(API.domain + API.course, { withCredentials: true })
      .map(this.extractData)
      .catch(this.handleError);
  }

  createCourse(course: Course) {
    return this._http.post(API.domain + API.course, course, { withCredentials: true })
      .map(this.extractData)
      .catch(this.handleError);
  }

  putCourse(course: Course) {
    return this._http.put(API.domain + API.course + '/' + course._id, course, this.options)
      .map(this.extractData)
      .catch(this.handleError);
  }

  deleteCourse(courseId: string) {
    return this._http.delete(API.domain + API.course + '/' + courseId, { withCredentials: true })
      .map(this.extractData)
      .catch(this.handleError);
  }

  search(conditions: Object, sortby?: string, order?: number) {
    var url = `${API.domain}${API.courseSearch}?sortby=${sortby}&order=${order}`;
    return this._http.post(url, conditions, { withCredentials: true })
      .map(this.extractData)
      .catch(this.handleError);
  }

  getLatestStatistics(courseId: string) {
    var url = API.domain + API.stringReplace(API.courseLatestStatistics, [courseId]);
    return this._http.get(url, this.options)
      .map(this.extractData)
      .catch(this.handleError);
  }

  getAllStatistics(courseId: string) {
    var url = API.domain + API.stringReplace(API.courseAllStatistics, [courseId]);
    return this._http.get(url, this.options)
      .map(this.extractData)
      .catch(this.handleError);
  }

}