import { Component, OnInit } from '@angular/core';

import { SignService } from '../../sign.service';
import { Sign, Course } from '../../../../shared';
import { LoginService } from '../../../../login';

@Component({
  selector: 'step2',
  templateUrl: './step2.component.html',
  styleUrls: ['./step2.component.css']
})
export class Step2Component implements OnInit {

  colors = ['#F5A623', '#97CC00', '#F8E71C', '#9013FE', '#58C9F3', '#FF6C60'];
  selectedColor = this.colors[0];
  sign: Sign;
  courses: Course[];
  selectedCourseId: any = '';

  constructor(
    private _loginService: LoginService,
    private _signService: SignService) {}

  ngOnInit() {
    this.sign = this._signService.sign;
    this.sign.color = this.selectedColor;        
    this._loginService.getCourses().subscribe(body => {
      if (+body.code == 200) {
        this.courses = body.data;
        this.selectedCourseId = this._signService.sign.relatedId ? this._signService.sign.relatedId : '';        
      } else {
        alert(body.msg);
      }
    }) 
  }

  selectColor(color: string) {
    this.selectedColor = color;
    this.sign.color = color;
  }

  selectCourse(courseId: string) {
    var selected = this.courses.filter((c: Course) => {
      return c._id == courseId;
    })[0];
    this._signService.selectRelatedCourse(selected);
    this.selectedCourseId = courseId;    
  }

}