import { Injectable } from '@angular/core';
import { Http } from '@angular/http';

import { BaseService, User, API, JsEncryptService } from '../shared';

@Injectable()
export class RegisterService extends BaseService {

  constructor(
    private _http: Http,
    private _jsEncryptService: JsEncryptService) {
      super();
  }

  register(phone: string, password: string, role: string) {
    var data = {
      phone,
      password: this._jsEncryptService.encrypt(password),
      role
    };
    return this._http.post(API.domain + API.user, data, { withCredentials: true })
      .map(this.extractData)
      .catch(this.handleError);
  }

}