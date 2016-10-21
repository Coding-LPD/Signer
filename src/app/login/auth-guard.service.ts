import { Injectable } from '@angular/core';
import { Router,
  CanActivate, 
  ActivatedRouteSnapshot, 
  RouterStateSnapshot
} from '@angular/router';

import { LoginService } from '../login'; 

@Injectable()
export class AuthGuardService implements CanActivate {

  constructor(
    private _router: Router,
    private _loginService: LoginService) {}

  canActivate(route: ActivatedRouteSnapshot, state: RouterStateSnapshot) {    
    if (this._loginService.isLoggedIn) return true;

    // 尚未登录，则保存当前url，并跳转到登录界面
    this._loginService.redirectUrl = state.url;    
    this._router.navigate(['/login']);
    return false;
  }

}