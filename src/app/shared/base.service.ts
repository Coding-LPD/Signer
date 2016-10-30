import { Injectable } from '@angular/core';
import { Response } from '@angular/http';
import { Observable } from 'rxjs/Observable';

@Injectable()
export class BaseService {  

  protected options = {
    withCredentials: true
  }

  protected extractData(res: Response) {
    let body = res.json();
    if (!body.code) {
      body = {
        code: '600',
        msg: '数据格式不对，解析失败'
      };
    }
    return body || { };
  }

  protected handleError(error: any) {
    let errMsg = (error.message) ? error.message : 
      error.status ? `${error.status} - ${error.statusText}` : `http请求异常`;    
    return Observable.of<any>({
      code: '600',
      msg: errMsg
    });
  }

}