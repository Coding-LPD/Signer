import { Injectable } from '@angular/core';
import { Http } from '@angular/http';

import { BaseService, API, SignStudent } from '../../shared';

@Injectable()
export class StudentService extends BaseService {

  constructor(private _http: Http) {
    super();
  }

  getStudent() {
    return this._http.get(API.domain + API.signStudent, { withCredentials: true })
      .map(this.extractData)
      .catch(this.handleError);
  }

  getUploadUrl() {
    return API.domain + API.signStudentImport;
  }

  createStudent(student: SignStudent) {
    return this._http.post(API.domain + API.signStudent, student, this.options)
      .map(this.extractData)
      .catch(this.handleError);
  }

  putStudent(student: SignStudent) {
    return this._http.put(API.domain + API.signStudent + '/' + student._id, student, this.options)
      .map(this.extractData)
      .catch(this.handleError);
  }

  remove(id: string) {
    return this._http.delete(API.domain + API.signStudent + `/${id}`, this.options)
      .map(this.extractData)
      .catch(this.handleError);
  }

  removeMulti(courseId: string, ids: string[]) {
    return this._http.post(API.domain + API.signStudentMutilRemove, { courseId, ids },  this.options)
      .map(this.extractData)
      .catch(this.handleError);
  }

  search(conditions: any) {
    return this._http.post(API.domain + API.signStudentSearch, conditions, this.options)
      .map(this.extractData)
      .catch(this.handleError);
  }

  searchRelatedInfo(number: string, signId: string) {
    return this._http.post(API.domain + API.studentRelatedInfo, { number, signId }, this.options)
      .map(this.extractData)
      .catch(this.handleError);
  }

}