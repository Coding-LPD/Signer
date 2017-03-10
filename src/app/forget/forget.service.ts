import { Injectable } from '@angular/core';
import { Http } from '@angular/http';
import { Observable } from 'rxjs/Observable';

import { BaseService, User, API, JsEncryptService } from '../shared';

@Injectable()
export class ForgetService extends BaseService {

  constructor(
    private _http: Http,
    private _jsEncryptService: JsEncryptService) {
      super();
  }

  changePassword(phone: string, password: string) {
    var url = API.domain + API.stringReplace(API.userPhone, [phone]);

    return this._jsEncryptService.encrypt(password)
      .flatMap(body => {
        if (body.code == 200) {
          return this._http.put(url, { password: body.data }, { withCredentials: true });
        }
        return Observable.of<any>(body);
      })
      .map(this.extractData)
      .catch(this.handleError);
  }

}