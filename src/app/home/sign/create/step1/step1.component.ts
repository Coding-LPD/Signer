import { Component, OnInit } from '@angular/core';
import { ActivatedRoute } from '@angular/router';

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
    signDate: '',
    startTime: '',
    endTime: ''
  }
  courses: Course[];
  selectedCourseId: any = '';

  constructor(
    private _route: ActivatedRoute,
    private _signService: SignService,
    private _loginService: LoginService) {}

  ngOnInit() {
    this.sign = this._signService.sign;
    this.setTime(this.sign.startTime, this.sign.endTime);
    this._loginService.getCourses().subscribe(body => {
      if (+body.code == 200) {
        this.courses = body.data;
        this.selectedCourseId = this._signService.sign.courseId ? this._signService.sign.courseId : '';
      } else {
        alert(body.msg);
      }
    });

    this._route.queryParams
      .subscribe(params => {
        this.extra.signDate = params['date'] || '';
      })
  }

  selectCourse(courseId: string) {
    var selected = this.courses.filter((c: Course) => {
      return c._id == courseId;
    })[0];
    this._signService.selectCourse(selected);   
    this.selectedCourseId = courseId;
  }

  selectTime(value: string, type: number) {
    switch (type) {
      case 0: 
        this.extra.signDate = value;
        break;
      case 1:
        this.extra.startTime = value;
        break;
      case 2:
        this.extra.endTime = value;
        break;
    }
    this.sign.startTime = `${this.extra.signDate} ${this.extra.startTime}`;
    this.sign.endTime = `${this.extra.signDate} ${this.extra.endTime}`;
  }

  setTime(startTime: string, endTime: string) {
    if (!startTime || !endTime) return;
    this.extra.signDate = startTime.split(' ')[0] || '';
    this.extra.startTime = startTime.split(' ')[1] || '';
    this.extra.endTime = endTime.split(' ')[1] || '';
  }

}