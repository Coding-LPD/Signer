import { Component, OnInit, ViewChild } from '@angular/core';
import { Router, ActivatedRoute, NavigationExtras } from '@angular/router';
import { Subject } from 'rxjs/Subject';
import { Observable } from 'rxjs/Observable';

import { SignStudent, Course, OperationOption, HeaderOption, CellOption, PopUpComponent } from '../../shared';
import { LoginService } from '../../login';
import { CourseService } from '../course';
import { StudentService } from './student.service';

@Component({
  selector: 'student',
  templateUrl: './student.component.html',
  styleUrls: ['./student.component.css']
})
export class StudentComponent implements OnInit {

  @ViewChild(PopUpComponent) popup: PopUpComponent;
  
  // 教师对应的所有课程
  courses: Course[];
  // 某课程下所有学生数据
  students: SignStudent[];
  // 当前选中的学生
  checkedStudents: SignStudent[] = [];
  // 加载中动画
  isLoading = false;
  // 文件上传指令的基本配置
  uploadOptions = {
    url: this._studentService.getUploadUrl(),
    data: { courseId: '' }
  };
  // list组件的配置
  operationOption: OperationOption = { hasCheck: true, hasEdit: true, hasDelete: true };
  headerOptions: HeaderOption[] = [
    { name: '学号' },
    { name: '姓名' },
    { name: '联系方式' },
    { name: '专业' },
    {
      name: '加入时间',
      width: '80px'      
    }
  ];
  cellOptions: CellOption[] = [
    { prop: 'number',     default: '无' },
    { prop: 'name',       default: '无' },
    { prop: 'phone',      default: '无' },
    { prop: 'major',      default: '无' },
    { prop: 'createdAt',  default: '无' } 
  ];
  // 查询流
  searchTerms = new Subject<string>();
  // 选择的课程组成的流
  selectedCourseIdSource = new Subject<string>();
  
  constructor(
    private _route: ActivatedRoute,
    private _router: Router,
    private _studentService: StudentService,
    private _loginService: LoginService,
    private _courseService: CourseService) {}

  ngOnInit() {
    this._loginService.getTeacherInfo().subscribe(body => {
      if (+body.code == 200) {
        var teacherId = body.data[0]._id;
        this._courseService.search({teacherId}, 'createdAt', 0).subscribe(body => {
          if (+body.code == 200) {
            this.courses = body.data;
          }
        })
      }
    });

    this.searchTerms
      .startWith('')
      .debounceTime(300)
      .distinctUntilChanged()
      .combineLatest(this.selectedCourseIdSource)
      .switchMap(arr => {
        let [term, courseId] = arr;
        if (courseId) {
          var conditions = { courseId };
          // 根据关键词的类型来判断是按学号还是按姓名来查询
          if (term) {
            if (!isNaN(Number(term))) {
              conditions['number'] = term;
            } else {
              conditions['name'] = term;
            }
          }
          return this._studentService.search(conditions);
        } else {
          return Observable.of({ code: 200, data: [] })
        }
      })
      .subscribe(body => {
        if (+body.code == 200) {         
          this.students = body.data;
          this.isLoading = false;
        } else {
          this.popup.show(body.msg);
        }
      });

    this._route.queryParams
      .subscribe(params => {
        var courseId = params['courseId'];
        if (courseId) {
          this.uploadOptions.data.courseId = courseId;
          this.selectedCourseIdSource.next(courseId);
        }
      });
  }

  searchStudent(term: string) {
    this.searchTerms.next(term);
  }

  selectCourse(courseId: string) {
    this.selectedCourseIdSource.next(courseId);
    this.uploadOptions.data.courseId = courseId;
    if (!courseId) {
      this.students = [];
      this.isLoading = false;
      return;
    }
    this.isLoading = true;
  }

  importStudent(fileInput: any) {
    if (!this.uploadOptions.data.courseId) {
      this.popup.show('请先选择课程');
      return;
    }
    // 触发文件上传控件
    fileInput.click();
  }

  exportStudent() {
    if (!this.uploadOptions.data.courseId) {
      this.popup.show('请先选择课程');
      return;
    }
    this._studentService.exportStudent(this.uploadOptions.data.courseId)
      .subscribe(body => {
        if (+body.code == 200) {
          window.open(body.data);
        } else {
          this.popup.show(body.msg);
        }
      });
  }

  handleUpload(data: any) {
    // 文件上传过程中持续调用该函数，所以要判断后台是否已经响应
    // 有响应的时候才解析得到的数据
    if (data.response) {      
      var res = JSON.parse(data.response);
      if (+res.code == 200) {
        this.students = this.students.concat(res.data);              
      } else {
        this.popup.show(res.msg);
      }
    }
  }

  receiveCheckedStudents(students: SignStudent[]) {
    this.checkedStudents = students;
  }

  addStudent() {
    if (!this.uploadOptions.data.courseId) {
      this.popup.show('请先选择课程');
      return;
    }
    var extra: NavigationExtras = {
      queryParams: { courseId: this.uploadOptions.data.courseId }
    };
    this._router.navigate(['/home/student/create'], extra);
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
          this.popup.show(body.msg);
        }
      });
  }

  editStudent(student: SignStudent) {
    this._router.navigate(['/home/student/edit', student._id]);
  }

  removeSelected() {
    if (this.checkedStudents.length <= 0) {
      this.popup.show('请先选择要删除的学生');
      return;
    }
    if (!confirm('确定删除这些学生？')) {
      return;
    }
    var courseId = this.checkedStudents[0].courseId;
    var ids = this.checkedStudents.map(student => student._id);
    this._studentService.removeMulti(courseId, ids)
      .subscribe(body => {
        if (body.code == 200) {
          this.selectedCourseIdSource.next(courseId);
        } else {
          this.popup.show(body.msg);
        }
      })
  }

}