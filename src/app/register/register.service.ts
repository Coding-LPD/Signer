import { Injectable } from '@angular/core';
import { Http } from '@angular/http';
import { Observable } from 'rxjs/Observable';

import { BaseService, User, API, JsEncryptService } from '../shared';

@Injectable()
export class RegisterService extends BaseService {

  constructor(
    private _http: Http,
    private _jsEncryptService: JsEncryptService) {
      super();
  }

  register(phone: string, password: string, role: string) {
    return this._jsEncryptService.encrypt(password)
      .flatMap(body => {
        if (body.code == 200) {
          var data = {
            phone,
            password: body.data,
            role
          };
          return this._http.post(API.domain + API.user, data, { withCredentials: true });
        }
        return Observable.of<any>(body);
      })
      .map(this.extractData)
      .catch(this.handleError);
  }

}