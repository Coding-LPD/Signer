import { Injectable, OnDestroy } from '@angular/core';
import { Http } from '@angular/http';
import { Observable } from 'rxjs/Observable';

import { BaseService } from './base.service';
import { API } from './api';

declare var JSEncrypt: any;

@Injectable()
export class JsEncryptService extends BaseService {

  private publickey: string;  

  constructor(private _http: Http) {
    super();
    this.getPublickey()
      .subscribe(body => {
        if (+body.code == 200) {
          this.publickey = body.data;
        } else {          
          alert('获取公钥失败');
        }    
      });    
  }

  encrypt(content: string): string {
    var encrypt = new JSEncrypt();    
    encrypt.setPublicKey(this.publickey);
    return encrypt.encrypt(content);
  }

  getPublickey(): Observable<any> {     
    // 已经获取了公钥，则直接返回
    if (this.publickey) {
      return Observable.of<any>({
        code: '200',
        data: this.publickey
      });
    }   
    // 未获取公钥，则发送请求获取
    return this._http.get(API.domain + API.publickey)
            .map(this.extractData)
            .catch(this.handleError)            
  }

}