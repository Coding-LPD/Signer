import { Component } from '@angular/core';
import { Router, ActivatedRoute, NavigationExtras } from '@angular/router';
import { Observable } from 'rxjs/Observable';

import { StudentService } from '../student.service';
import { SignStudent } from '../../../shared';

@Component({
  selector: 'create',
  templateUrl: './create.component.html',
  styleUrls: ['./create.component.css']
})
export class CreateComponent {
  
  student = new SignStudent();
  isEdit = false;

  constructor(
    private _route: ActivatedRoute,
    private _router: Router,
    private _studentService: StudentService) {}

  ngOnInit() {
    this._route.params
      .flatMap(params => {
        if (params['id']) {
          this.isEdit = true;
          return this._studentService.search({ _id: params['id'] });
        } else {
          return Observable.of({ code: 600 })
        }
      })
      .subscribe(body => {
        if (+body.code == 200) {
          this.student = body.data[0];
        }
      });

    this._route.queryParams
      .subscribe(params => {
        if (params['courseId']) {
          this.student.courseId = params['courseId'];
        }
      });
  }

  saveStudent() {
    if (!this.student.number || !this.student.number.trim()) {
      alert('学号不能为空');
      return;
    }
    if (!this.student.name || !this.student.name.trim()) {
      alert('姓名不能为空');
      return;
    }
    var tip = this.isEdit ? '修改成功' : '创建成功';
    var observable = this.isEdit ? this._studentService.putStudent(this.student) 
                      : this._studentService.createStudent(this.student);
    
    observable.subscribe(body => {
      if (+body.code == 200) {
        alert(tip);
        this.backToList();
      } else {
        alert(body.msg);
      }
    });
  }

  cancelEdit() {
    this.backToList();
  }

  private backToList() {
    var extra: NavigationExtras = { 
      queryParams: { courseId: this.student.courseId }
    }; 
    this._router.navigate(['/home/student'], extra);
  }

}