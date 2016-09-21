import { Injectable } from '@angular/core';
import { Http } from '@angular/http';

import { BaseService, API } from '../../shared';

@Injectable()
export class TeacherService extends BaseService {

  constructor(private _http: Http) {
    super();
  }

  search(conditions: Object) {
    return this._http.post(API.domain + API.teacherSearch, conditions, { withCredentials: true })
      .map(this.extractData)
      .catch(this.handleError);
  }

}