import { Component, OnInit, ViewChild } from '@angular/core';
import { Router, ActivatedRoute } from '@angular/router';
import { Observable } from 'rxjs/Observable';

import { Course, PopUpComponent } from '../../../shared';
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

  @ViewChild(PopUpComponent) popup: PopUpComponent;

  private _defaultTime = '星期一 1节-2节';
  course = new Course();  
  courseTimes: WrapString[] = [{value:this._defaultTime}];
  isEdit = false;

  constructor(
    private _route: ActivatedRoute,
    private _router: Router,
    private _loginService: LoginService,
    private _courseService: CourseService) {}

  ngOnInit() {
    this._loginService.getTeacherInfo().subscribe(body => {
      if (+body.code == 200) {
        this.course.teacherId = body.data[0]._id;        
      }
    });

    this._route.params
      .flatMap(params => {
        if (params['id']) {
          this.isEdit = true;
          return this._courseService.search({ _id: params['id'] });
        } else {
          return Observable.of({ code: 600 })
        }
      })
      .subscribe(body => {
        if (+body.code == 200) {
          this.course = body.data[0];
          var times = this.course.time.split(',');
          this.courseTimes = times.map(t => { return { value: t } });
        }
      });
  }

  saveCourse(course: Course) {
    if (this.courseTimes.length <= 0) {
      this.popup.show('请输入至少一个上课时间');
      return;
    }
    var times = this.courseTimes.map(t => t.value);
    course.time = times.join(',');
    var tip = this.isEdit ? '修改成功' : '创建成功';
    var observable = this.isEdit ? this._courseService.putCourse(course) 
                      : this._courseService.createCourse(course);
    
    observable.subscribe(body => {
      if (+body.code == 200) {
        this.popup.show(tip);
        this._router.navigate(['/home/course']);
      } else {
        this.popup.show(body.msg);
      }
    });
  }

  addClassTime() {
    this.courseTimes.push({value:this._defaultTime});
  }

  removeClassTime(index: number) {
    this.courseTimes.splice(index, 1);
  }

  customTrackBy(index: number, obj: any): any {
    return index;
  }

  cancelEdit() {
    this._router.navigate(['/home/course']);
  }

}