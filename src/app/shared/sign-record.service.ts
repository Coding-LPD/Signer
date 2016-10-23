import { Injectable } from '@angular/core';
import { Http } from '@angular/http';

import { API } from './api';
import { BaseService } from './base.service';

@Injectable()
export class SignRecordService extends BaseService {

  constructor(private _http: Http) {
    super();
  }

  agree(id: string) {
    var url = API.domain + API.stringReplace(API.signRecordAssent, [id]);
    return this._http.post(url, null)
      .map(this.extractData)
      .catch(this.handleError);
  }

  refuse(id: string) {
    var url = API.domain + API.stringReplace(API.signRecordRefusal, [id]);
    return this._http.post(url, null)
      .map(this.extractData)
      .catch(this.handleError);
  }

  search(conditions: Object) {
    return this._http.post(API.domain + API.signRecordSearch, conditions)
      .map(this.extractData)
      .catch(this.handleError);      
  }

}