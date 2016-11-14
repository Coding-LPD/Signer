import { Injectable } from '@angular/core';
import { Http } from '@angular/http';

import { API } from './api';
import { BaseService } from './base.service';

@Injectable()
export class SignRecordService extends BaseService {

  constructor(private _http: Http) {
    super();
  }

  confirm(id: string, type: number = 0) {
    var url = API.domain;
    if (type == 0) {
      url += API.stringReplace(API.signRecordAssent, [id]);
    } else {
      url += API.stringReplace(API.signRecordRefusal, [id]);
    }
    return this._http.post(url, null)
      .map(this.extractData)
      .catch(this.handleError);
  } 

  confirmAll(recordIds: string[], type: number = 0) {
    var url = API.domain;
    url += type == 0 ? API.signRecordAssentAll : API.signRecordRefusalAll;
    return this._http.post(url, { recordIds })
      .map(this.extractData)
      .catch(this.handleError);
  }

  search(conditions: Object) {
    return this._http.post(API.domain + API.signRecordSearch, conditions)
      .map(this.extractData)
      .catch(this.handleError);      
  }

  addition(signId: string, courseId: string, number: string, type: number) {
    return this._http.post(API.domain + API.signRecordAddition, { signId, courseId, number, type })
      .map(this.extractData)
      .catch(this.handleError);
  }

}