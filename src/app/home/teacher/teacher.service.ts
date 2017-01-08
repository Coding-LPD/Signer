import { Injectable } from '@angular/core';
import { Http } from '@angular/http';

import { BaseService, API } from '../../shared';

@Injectable()
export class TeacherService extends BaseService {

  constructor(private _http: Http) {
    super();
  }

  getSignsInPeriod(teacherId: string, startTime: string, endTime: string) {
    var url = API.stringReplace(API.domain + API.teacherSignsInPeriod, [teacherId]);
    return this._http.post(url, { startTime, endTime }, this.options)
      .map(this.extractData)
      .catch(this.handleError);
  }

  search(conditions: Object) {
    return this._http.post(API.domain + API.teacherSearch, conditions, { withCredentials: true })
      .map(this.extractData)
      .catch(this.handleError);
  }

}