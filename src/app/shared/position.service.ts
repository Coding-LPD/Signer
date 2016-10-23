import { Injectable } from '@angular/core';
import { Http } from '@angular/http';

import { API } from './api';
import { BaseService } from './base.service';

@Injectable() 
export class PositionService extends BaseService {

  constructor(private _http: Http) {
    super();
  }

  getIP() {
    return this._http.get(API.ipService)
      .map(this.extractData)
      .catch(this.handleError);
  }

  locate(ip: string, teacherId: string, signId: string) {
    return this._http.post(API.domain + API.position, { ip, teacherId, signId })
      .map(this.extractData)
      .catch(this.handleError);
  }

}