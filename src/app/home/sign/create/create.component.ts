import { Component } from '@angular/core';
import { Router } from '@angular/router';

import { Sign } from '../../../shared';
import { SignService } from '../sign.service';

@Component({
  selector: 'create',
  templateUrl: './create.component.html',
  styleUrls: ['./create.component.css'],
  providers: [SignService]
})
export class CreateComponent {
  
  // 控制界面的显示
  steps = [true, false, false];
  complete = false;

  constructor(
    private _router: Router,
    private _signService: SignService) {}

  toNextStep() {
    var currentStep = this.steps.indexOf(true);
    this.steps[currentStep] = false;
    this.steps[(currentStep + 1) % this.steps.length] = true;
    this._router.navigate([`/home/sign/create/step${currentStep+2}`]);
  }

  toPreviousStep() {
    var currentStep = this.steps.indexOf(true);
    this.steps[currentStep] = false;
    this.steps[(currentStep - 1) % this.steps.length] = true;
    this._router.navigate([`/home/sign/create/step${currentStep}`]);
  }

  toComplete() {
    this._signService.create(this._signService.sign)
      .subscribe(body => {
        if (+body.code == 200) {
          this._signService.sign = body.data;
          alert('创建成功');
          this.complete = true;
          this._router.navigate(['/home/sign/create/complete']);
        } else {
          alert(body.data);
        }
      })    
  }

}