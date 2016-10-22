import { Injectable } from '@angular/core';
import { Http } from '@angular/http';

import { API } from './api';
import { BaseService } from './base.service';

@Injectable() 
export class IPService extends BaseService {

  constructor(private _http: Http) {
    super();
  }

  getIP() {
    return this._http.get(API.ipService)
      .map(this.extractData)
      .catch(this.handleError);
  }

}