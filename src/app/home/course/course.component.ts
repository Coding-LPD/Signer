import { Component, OnInit, ViewChild } from '@angular/core';
import { Router } from '@angular/router';
import { Subject } from 'rxjs/Subject';
import { Observable } from 'rxjs/Observable';

import { Course, Teacher, OperationOption, HeaderOption, CellOption, PopUpComponent } from '../../shared';
import { LoginService } from '../../login';
import { TeacherService } from '../teacher';
import { CourseService } from './course.service';

@Component({
  selector: 'course',
  templateUrl: './course.component.html',
  styleUrls: ['./course.component.css']
})
export class CourseComponent implements OnInit {

  @ViewChild(PopUpComponent) popup: PopUpComponent;

  // 所有课程数据
  courses: Course[];
  // list组件的配置  
  operationOption: OperationOption = { hasEdit:true, hasDelete: true };
  headerOptions: HeaderOption[] = [
    { name: '课程名称' },
    { name: '上课时间', width: '200px' },
    { name: '学生数量' },
    { name: '签到次数' },
    { name: '上课地点', width: '80px' }
  ];
  cellOptions: CellOption[] = [
    { prop: 'name',         default: '无' },
    { prop: 'time',         default: '无' },
    { prop: 'studentCount', default: '0'  },
    { prop: 'signCount',    default: '0'  },
    { prop: 'location',     default: '无' }
  ];
  // 查询流
  searchTerms = new Subject<string>();

  constructor(
    private _router: Router,
    private _loginService: LoginService,
    private _teacherService: TeacherService, 
    private _courseService: CourseService) { }

  ngOnInit() {

    // 输入课程名搜索
    this.searchTerms
      .startWith('')
      .debounceTime(300)
      .distinctUntilChanged()
      .combineLatest(this._loginService.getTeacherInfo())
      .switchMap(arr => {
        let [term, body] = arr;
        var condition = {};
        if (+body.code == 200) {          
          if (term) {
            condition['name'] = term;
          }
          condition['teacherId'] = body.data[0]._id;
          return this._courseService.search(condition, 'createdAt', -1);
        } else {
          return Observable.of(body);
        }
      })
      .subscribe(body => {
        if (+body.code == 200) {
          this.courses = body.data;
        } else {
          this.popup.show(body.msg);
        }
      });
  }

  createCourse() {
    this._router.navigate(['/home/course/create']);
  }

  searchCourse(name: string) {
    this.searchTerms.next(name);
  }

  editCourse(course: Course) {
    this._router.navigate(['/home/course/edit', course._id]);
  }

  removeCourse(course: Course) {
    if (!confirm('确定删除该课程吗？')) {
      return;
    }
    this._courseService.deleteCourse(course._id) 
      .subscribe(body => {
        if (+body.code == 200) {
          this.popup.show('删除成功');
          var index = this.courses.indexOf(course);
          this.courses.splice(index, 1);
          this.courses = this.courses.slice(0);
        } else {
          this.popup.show(body.msg);
        }
      });
  }
}