import { Injectable } from '@angular/core';
import { Http } from '@angular/http';

import { BaseService, API } from '../../services';

@Injectable()
export class SmsCodeService extends BaseService {

  constructor(private _http: Http) {
    super();
  }

  sendSmsCode(phone: string) {    
    return this._http.post(API.domain + API.smsCode, { phone })
      .map(this.extractData)
      .catch(this.handleError);
  }

  verifySmsCode(phone: string, smsCode: string) {
    return this._http.post(API.domain + API.smsCodeVerification, { phone, smsCode })
      .map(this.extractData)
      .catch(this.handleError);
  }

}