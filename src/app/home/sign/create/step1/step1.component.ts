import { Component, OnInit } from '@angular/core';

import { Sign, Course } from '../../../../shared';
import { LoginService } from '../../../../login';
import { SignService } from '../../sign.service';

@Component({
  selector: 'step1',
  templateUrl: './step1.component.html',
  styleUrls: ['./step1.component.css']  
})
export class Step1Component implements OnInit {
  
  sign: Sign;
  extra = {
    startTime: '',
    endTime: ''
  }
  courses: Course[];
  selectedCourse: any = '';

  constructor(
    private _signService: SignService,
    private _loginService: LoginService) {}

  ngOnInit() {
    this.sign = this._signService.sign;
    this._loginService.getCourses().subscribe(body => {
      if (+body.code == 200) {
        this.courses = body.data;
        this.selectedCourse = this._signService.course ? this._signService.course : '';
      } else {
        alert(body.data);
      }
    })
  }

  selectCourse(course: Course) {
    this._signService.selectCourse(course);        
    this.selectedCourse = course;
  }

}