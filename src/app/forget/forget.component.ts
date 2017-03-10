import { Component, ViewChild } from '@angular/core';
import { Router } from '@angular/router';

import { LoginService } from '../login';
import { User, PopUpComponent, SmsCodeService } from '../shared';
import { ForgetService } from './forget.service';

@Component({
  selector: 'forget',
  templateUrl: './forget.component.html',
  styleUrls: ['./forget.component.css']
})
export class ForgetComponent {

  @ViewChild(PopUpComponent) popup: PopUpComponent;
  
  user: User = new User('', '', '1');
  extra = {
    smsid: '',
    smsCode: '',
    comfirmPassword: ''
  };

  constructor(
    private _smsCodeService: SmsCodeService,
    private _forgetService: ForgetService,
    private _loginService: LoginService,
    private _router: Router) {}

  submit(user: User) {    
    this._smsCodeService.verifySmsCode(this.user.phone, this.extra.smsCode)
      .subscribe(body => {
        if (+body.code == 200) {
          // 验证码验证成功才能修改密码
          this.changePassword(user);
        } else {
          this.popup.show(body.msg);
        }
      });
  }

  changePassword(user: User) {
    this._forgetService.changePassword(user.phone, user.password)
      .subscribe(body => {
        // 修改成功则提示成功，否则提示错误信息
        var tip = body.data;
        if (+body.code == 200) {
          tip = '修改密码成功';
          setTimeout(() => this._router.navigate(['/login']), 1500);
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