import { Injectable } from '@angular/core';
import { Http } from '@angular/http';

import { BaseService } from './base.service';
import { API } from './api';

@Injectable() 
export class UserService extends BaseService {

  constructor(private _http: Http) {
    super();
  }

  find(conditions: Object) {    
    return this._http.post(API.domain + API.userSearch, conditions)
      .map(this.extractData)
      .catch(this.handleError);      
  }

}