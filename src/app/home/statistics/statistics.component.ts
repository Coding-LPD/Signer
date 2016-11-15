import { Component, OnInit } from '@angular/core';

import { LoginService } from '../../login';
import { Course } from '../../shared';
import { StatisticsService } from './statistics.service';

@Component({
  selector: 'statistics',
  templateUrl: './statistics.component.html',
  styleUrls: ['./statistics.component.css']
})
export class StatisticsComponent implements OnInit {

  isOpenSelect = false;
  selectedCourse: Course;
  courses: Course[] = [];

  constructor(
    private _loginService: LoginService,
    private _statisticsService: StatisticsService
  ) {}

  ngOnInit() {
    this._loginService.getCourses()
      .subscribe(body => {
        if (+body.code == 200) {
          this.courses = body.data;
        } else {
          alert(body.msg);
        }
      });    
  }

  toggleSelect() {
    this.isOpenSelect = !this.isOpenSelect;
  }

  selectCourse(course: Course) {    
    this.isOpenSelect = false;
    this.selectedCourse = course;
    this._statisticsService.selectCourse(course);
  }

}