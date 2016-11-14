import { Component, OnInit } from '@angular/core';
import { Router, ActivatedRoute, Params } from '@angular/router';
import { Subject } from 'rxjs/Subject';
import { Observable } from 'rxjs/Observable';

import { Sign, SignRecordService } from '../../../shared';
import { StudentService } from '../../student';
import { SignService } from '../sign.service';

@Component({
  selector: 'add-sign-in',
  templateUrl: './add-sign-in.component.html',
  styleUrls: ['./add-sign-in.component.css']
}) 
export class AddSignInComponent implements OnInit {

  searchTerms = new Subject<string>();
  sign: Sign;
  student = {
    number: '',
    name: '请输入学号',
    nickname: '请输入学号'
  };  

  constructor(
    private _route: ActivatedRoute,
    private _router: Router,
    private _studentService: StudentService,
    private _signService: SignService,
    private _signRecordService: SignRecordService  
  ) {}

  ngOnInit() {
    this._route.params.forEach((params: Params) => {
      var signId = params['id'];

      // 获取签到详情             
      this._signService.search({ _id: signId })
        .subscribe(body => {          
          if (+body.code == 200) {            
            this.sign = body.data[0];  
          } else {
            alert(body.msg);
          }
        });

      // 输入学号进行搜索
      this.searchTerms
        .debounceTime(300)
        .distinctUntilChanged()
        .switchMap((term: string) => {
          if (term) {
            return this._studentService.searchRelatedInfo(term, signId);
          } else {
            return Observable.of<any>({ 
              data: {
                name: '请输入学号',
                nickname: '请输入学号',
              }
            });
          }          
        })
        .subscribe(body => {
          this.student.name = body.data.name;
          this.student.nickname = body.data.nickname;
        });
    });    
  }

  searchStudent(number: string) {
    this.student.number = number;
    this.searchTerms.next(number);
  }

  addSignIn() {
    this._signRecordService.addition(this.sign._id, this.sign.courseId, this.student.number, null)
      .subscribe(body => {
        if (+body.code == 200) {
          alert('补签成功');
          this._router.navigate(['/home/sign', this.sign._id, 'detail']);
        } else {
          alert(body.msg);
        }        
      })
  }

}