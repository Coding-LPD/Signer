import { Injectable } from '@angular/core';
import { Http } from '@angular/http';

import { BaseService, API } from '../../shared';

@Injectable()
export class StudentService extends BaseService {

  constructor(private _http: Http) {
    super();
  }

  getStudent() {
    return this._http.get(API.domain + API.student, { withCredentials: true })
      .map(this.extractData)
      .catch(this.handleError);
  }

  getUploadUrl() {
    return API.domain + API.studentImport;
  }

  search(conditions: any) {
    return this._http.post(API.domain + API.studentSearch, conditions, { withCredentials: true })
      .map(this.extractData)
      .catch(this.handleError);
  }

}