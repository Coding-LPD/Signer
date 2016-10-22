import { Component, ViewChild } from '@angular/core';
import { Router } from '@angular/router';

import { LoginService } from '../login';
import { User, PopUpComponent, SmsCodeService } from '../shared';
import { RegisterService } from './register.service';

@Component({
  selector: 'register',
  templateUrl: './register.component.html',
  styleUrls: ['./register.component.css']
})
export class RegisterComponent {

  @ViewChild(PopUpComponent) popup: PopUpComponent;
  
  user: User = new User('', '', '1');
  extra = {
    smsid: '',
    smsCode: '',
    comfirmPassword: ''
  };

  constructor(
    private _smsCodeService: SmsCodeService,
    private _registerService: RegisterService,
    private _loginService: LoginService,
    private _router: Router) {}

  submit(user: User) {    
    this._smsCodeService.verifySmsCode(this.user.phone, this.extra.smsCode)
      .subscribe(body => {
        if (+body.code == 200) {
          // 验证码验证成功才能进行注册
          this.register(user);
        } else {
          this.popup.show(body.msg);
        }
      });
  }

  register(user: User) {
    this._registerService.register(user.phone, user.password, user.role)
      .subscribe(body => {
        // 注册成功则提示成功，否则提示错误信息
        var tip = body.data;
        if (+body.code == 200) {
          tip = '注册成功';
          this._loginService.user = body.data.user;
          this._loginService.isLoggedIn = true;
          setTimeout(() => this._router.navigate(['/home/calendar']), 1500);
        }
        this.popup.show(tip);
      });
  }

  /**
   * 弹框显示错误信息
   */
  handleError($event: any) {
    this.popup.show($event);
  }

}