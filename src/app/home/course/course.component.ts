import { Component, OnInit } from '@angular/core';
import { Router } from '@angular/router';

import { Course, Teacher, OperationOption, HeaderOption, CellOption } from '../../shared';
import { LoginService } from '../../login';
import { TeacherService } from '../teacher';
import { CourseService } from './course.service';

@Component({
  selector: 'course',
  templateUrl: './course.component.html',
  styleUrls: ['./course.component.css']
})
export class CourseComponent implements OnInit {

  // 所有课程数据
  courses: Course[];
  // list组件的配置  
  operationOption: OperationOption = { hasEdit:true, hasDelete: true };
  headerOptions: HeaderOption[] = [
    { name: '课程名称' }, { name: '上课时间', width: '200px' }, { name: '学生数量' }, 
    { name: '签到次数' }, { name: '上课地点', width: '80px' }
  ];
  cellOptions: CellOption[] = [
    { prop: 'name' },    { prop: 'time' }, { prop: 'studentCount', default: '0' }, 
    { prop: 'signCount', default: '0' },   { prop: 'location' },
  ];

  constructor(
    private _router: Router,
    private _loginService: LoginService,
    private _teacherService: TeacherService, 
    private _courseService: CourseService) { }

  ngOnInit() {
    this._loginService.getTeacherInfo().subscribe(body => {
      if (+body.code == 200) {
        var teacherId = body.data[0]._id;
        this._courseService.search({teacherId}, 'createdAt', -1).subscribe(body => {
          if (+body.code == 200) {
            this.courses = body.data;
          } else {
            alert(body.msg);
          }
        });
      } else {
        alert(body.msg);
      }
    })
  }

  createCourse() {
    this._router.navigate(['/home/course/create']);
  }

  receiveCheckedCourses(courses: Course[]) {
    console.log(courses);
  }

  editCourse(course: Course) {

  }

  removeCourse(course: Course) {
    if (!confirm('确定删除该课程吗？')) {
      return;
    }
    this._courseService.deleteCourse(course._id) 
      .subscribe(body => {
        if (+body.code == 200) {
          alert('删除成功');
          var index = this.courses.indexOf(course);
          this.courses.splice(index, 1);
          this.courses = this.courses.slice(0);
        } else {
          alert(body.msg);
        }
      });
  }
}