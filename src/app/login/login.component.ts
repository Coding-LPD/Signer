import { Component, ViewChild } from '@angular/core';
import { Observable } from 'rxjs/Observable';
import { Router, NavigationExtras } from '@angular/router';

import { User, PopUpComponent } from '../shared';
import { LoginService } from './login.service';

@Component({
  selector: 'login',
  templateUrl: './login.component.html',
  styleUrls: ['./login.component.css']
})
export class LoginComponent {

  @ViewChild(PopUpComponent) popup: PopUpComponent;

  user: User = new User('', '', '');
  extra = {
    smsCode: '',
    smsid: ''    
  };
  isPasswordLogin = true;
  active = true;

  constructor(
    private _router: Router,
    private _loginService: LoginService) {}

  login(user: User) {
    var redirectUrl = this._loginService.redirectUrl ? this._loginService.redirectUrl : '/home/calendar';
    var extra: NavigationExtras = { replaceUrl: true };
    if (this.isPasswordLogin) {
      // 密码登录
      this._loginService.loginWithPassword(user.phone, user.password)
        .subscribe(body => {
          if (+body.code == 200) {
            this.popup.show('登录成功');
            setTimeout(() => this._router.navigate([redirectUrl], extra), 1500);            
          } else {
            this.popup.show(body.msg);
          }
        }); 
    } else {
      // 验证码登录
      this._loginService.loginWithSmsCode(user.phone, this.extra.smsCode)
        .subscribe(body => {
          if (+body.code == 200) {
            this.popup.show('登录成功');  
            setTimeout(() => this._router.navigate([redirectUrl], extra), 1500);          
          } else {
            this.popup.show(body.msg);
          }
        });
    }       
  }

  resetPassword($event: any) {
    $event.preventDefault();
    alert('暂未开放该功能');
  }

  handleError($event: any) {
    this.popup.show($event);
  }

  toggleLoginMode($event: any) {
    $event.preventDefault();
    this.isPasswordLogin = !this.isPasswordLogin;
    this.refreshForm();
  }

  refreshForm() {
    this.active = false;
    setTimeout(() => this.active = true, 0);
  }

}