import { Component, OnInit } from '@angular/core';

import { SignService } from '../../sign.service';
import { Sign, Course } from '../../../../shared';

@Component({
  selector: 'step3',
  templateUrl: './step3.component.html',
  styleUrls: ['./step3.component.css']
})
export class Step3Component implements OnInit {

  sign: Sign;
  course: Course;
  relatedCourse: Course;

  constructor(private _signService: SignService) {}

  ngOnInit() {
    this.sign = this._signService.sign;
    this.course = this._signService.course;
    this.relatedCourse = this._signService.relatedCourse;
  }

}