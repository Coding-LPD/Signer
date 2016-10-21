import { Component, OnInit } from '@angular/core';
import { Router } from '@angular/router';

import { Course } from '../../../shared';
import { LoginService } from '../../../login';
import { CourseService } from '../course.service';

interface WrapString {
  value: string;
}

@Component({
  selector: 'create',
  templateUrl: './create.component.html',
  styleUrls: ['./create.component.css']
})
export class CreateComponent implements OnInit {

  private _defaultTime = '星期一 1节-2节';
  course = new Course();  
  courseTimes: WrapString[] = [{value:this._defaultTime}];

  constructor(
    private _router: Router,
    private _loginService: LoginService,
    private _courseService: CourseService) {}

  ngOnInit() {
    this._loginService.getTeacherInfo().subscribe(body => {
      if (+body.code == 200) {
        this.course.teacherId = body.data[0]._id;        
      }
    });
  }

  saveCourse(course: Course) {    
    course.time = this.courseTimes.join(',');
    this._courseService.createCourse(course)
      .subscribe(body => {
        if (+body.code == 200) {
          alert('创建成功');
          this._router.navigate(['/home/course']);
        } else {
          alert(body.msg);
        }
      });
  }

  addClassTime() {
    this.courseTimes.push({value:this._defaultTime});
  }

  customTrackBy(index: number, obj: any): any {
    return index;
  }

}