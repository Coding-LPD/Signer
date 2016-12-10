import { Component, OnInit } from '@angular/core';

import { SignStudent, Course, OperationOption, HeaderOption, CellOption } from '../../shared';
import { LoginService } from '../../login';
import { CourseService } from '../course';
import { StudentService } from './student.service';

@Component({
  selector: 'student',
  templateUrl: './student.component.html',
  styleUrls: ['./student.component.css']
})
export class StudentComponent implements OnInit {
  
  // 教师对应的所有课程
  courses: Course[];
  // 某课程下所有学生数据
  students: SignStudent[];
  // 文件上传指令的基本配置
  uploadOptions = {
    url: this._studentService.getUploadUrl(),
    data: { courseId: '' }
  };
  // list组件的配置
  operationOption: OperationOption = { hasCheck: true, hasEdit: true, hasDelete: true };
  headerOptions: HeaderOption[] = [
    { name: '学号' }, { name: '姓名' }, { name: '联系方式' }, { name: '专业' },
    { 
      name: '加入时间',
      width: '80px'      
    }
  ];
  cellOptions: CellOption[] = [
    { prop: 'number' }, { prop: 'name' }, { prop: 'phone' }, { prop: 'major' }, { prop: 'createdAt' } 
  ];
  

  constructor(
    private _studentService: StudentService,
    private _loginService: LoginService,
    private _courseService: CourseService) {}

  ngOnInit() {
    this._loginService.getTeacherInfo().subscribe(body => {
      if (+body.code == 200) {
        var teacherId = body.data[0]._id;
        this._courseService.search({teacherId}).subscribe(body => {
          if (+body.code == 200) {
            this.courses = body.data;
          }
        })
      }
    });
  }

  selectCourse(courseId: string) {
    this.uploadOptions.data.courseId = courseId;
    if (!courseId) {
      this.students = [];      
      return;
    }
    this._studentService.search({courseId})
      .subscribe(body => {
        if (+body.code == 200) {         
          this.students = body.data;
        } else {
          alert(body.msg);
        }
      });    
  }

  importStudent(fileInput: any) {
    if (!this.uploadOptions.data.courseId) {
      alert('请先选择课程');
      return;
    }
    // 触发文件上传控件
    fileInput.click();
  }

  handleUpload(data: any) {
    // 文件上传过程中持续调用该函数，所以要判断后台是否已经响应
    // 有响应的时候才解析得到的数据
    if (data.response) {      
      var res = JSON.parse(data.response);
      if (+res.code == 200) {
        this.students = this.students.concat(res.data);              
      } else {
        alert(res.msg);
      }
    }
  }

  receiveCheckedStudents(students: SignStudent[]) {
    console.log(students);
  }

  removeStudent(student: SignStudent) {
    if (!confirm('确定删除该学生？')) {
      return;
    }
    this._studentService.remove(student._id)
      .subscribe(body => {
        if (+body.code == 200) {
          var index = this.students.indexOf(student);
          this.students.splice(index, 1);
          this.students = this.students.slice(0);          
        } else {
          alert(body.msg);
        }
      })
  }

}