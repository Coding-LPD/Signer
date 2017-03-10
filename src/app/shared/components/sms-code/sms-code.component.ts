import { Component, Input, Output, EventEmitter } from '@angular/core';
import { Observable } from 'rxjs/Observable';

import { SmsCodeService } from './sms-code.service';
import { UserService } from '../../services';

@Component({
  selector: 'sms-code',
  template: `
    <button class="sms-code-btn" type="button" [disabled]="isDisabled" (click)="sendSmsCode()">{{title}}</button>
  `,
  styles: [`
    .sms-code-btn {
      padding: 11px 0;
      color: white;
      background-color: #44CCFE;  
    }
    .sms-code-btn:active {
      background-color: #2f8eb1;
    }
    .sms-code-btn[disabled] {
      background-color: #8fe0fe;
    }
  `]
})
export class SmsCodeComponent {

  private _defaultTitle = '获取验证码';
  private _defaultWait = 60;
  title = this._defaultTitle;
  @Input() phone: string;
  @Input() isDisabled = false;
  @Input() checkUserExist = false;
  @Output() onSuccess = new EventEmitter<string>();
  @Output() onFail = new EventEmitter<string>();

  constructor(
    private _smsCodeService: SmsCodeService,
    private _userService: UserService
  ) {}

  /**
   * 发送验证码短信
   * 成功：触发onSuccess，返回smsid
   * 失败：触发onFail，返回错误信息
   */
  sendSmsCode() {
    var interval = this.setWaitState();
    var observable = Observable.of<any>({ code: 200, data: [] });

    if (this.checkUserExist) {
      // 检测手机号码是否已经注册，没有才发送短信验证码
      observable = this._userService.find({phone: this.phone});
    }

    observable.flatMap(body => {
      if (+body.code == 200) {
        if (body.data.length == 0) {
          // 该手机号码可以注册
          return this._smsCodeService.sendSmsCode(this.phone);
        } else {
          // 手机号码已被注册，重置发送按钮状态
          clearInterval(interval);
          this.isDisabled = false;
          this.title = this._defaultTitle;
          return Observable.of<any>({ code: 600, data: '该手机号码已被注册' });
          // this.onFail.emit('该手机号码已被注册');
        }
      } else {
        // 查找出错，返回错误信息
        return Observable.of<any>(body);
        // this.onFail.emit(body.data);
      }
    }).subscribe(body => {
      if (+body.code == 200) {
        // 发送验证码成功，返回smsid
        this.onSuccess.emit(body.data);
      } else if (+body.code == 1004 || +body.code == 3002) {
        // 手机号码无效或者达到发送短信上限，重置发送按钮状态，返回错误信息
        clearInterval(interval);
        this.isDisabled = false;
        this.title = this._defaultTitle;
        this.onFail.emit(body.data);
      } else {
        // 其他错误，返回错误信息
        this.onFail.emit(body.data);
      }
    })   
  }

  /**
   * 将发送按钮置为冷却状态，等待时间结束后重置按钮状态
   */
  private setWaitState() {
    var wait = this._defaultWait;
    this.isDisabled = true;
    this.title = `${wait}秒`;

    // 冷却倒计时
    var interval = setInterval(() => {      
      // 冷却结束，按钮状态重置
      if (wait == 0) {
        clearInterval(interval);
        this.isDisabled = false;
        this.title = this._defaultTitle;
        return;
      }
      // 冷却中，随时时间改变按钮文本
      wait--;
      this.title = wait < 10 ? `0${wait}秒` : `${wait}秒`;
    }, 1000);

    return interval;
  }

}