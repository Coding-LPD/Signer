import { Injectable } from '@angular/core';
import { Http } from '@angular/http';

import { API } from './api';
import { BaseService } from './base.service';

@Injectable()
export class SignRecordService extends BaseService {

  constructor(private _http: Http) {
    super();
  }

  search(conditions: Object) {
    return this._http.post(API.domain + API.signRecordSearch, conditions)
      .map(this.extractData)
      .catch(this.handleError);      
  }

}