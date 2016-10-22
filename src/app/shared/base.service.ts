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
    if (!body.code || +body.code != 200) {
      console.log(`code:${body.code}, msg:${body.msg}`);
    }
    return body || { };
  }

  protected handleError(error: any) {
    let errMsg = (error.message) ? error.message : 
      error.status ? `${error.status} - ${error.statusText}` : `Server error`;
    console.error(errMsg);
    return Observable.throw(errMsg);
  }

}