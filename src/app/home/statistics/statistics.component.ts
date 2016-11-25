import { Component, OnInit } from '@angular/core';
import { Router, NavigationEnd } from '@angular/router';

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
    private _router: Router,
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

    // 监听路由事件中的结束事件，以便向即将路由进去的子组件发送当前选中的课程
    this._router.events
      .filter((event: any) => event instanceof NavigationEnd)
      .subscribe((event: any) => {
        this._statisticsService.selectCourse(this.selectedCourse);
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