import { Component, OnInit, ViewChild } from '@angular/core';
import { Router, ActivatedRoute } from '@angular/router';
import { Subject } from 'rxjs/Subject';
import { Observable } from 'rxjs/Observable';
import * as moment from 'moment';

import { Sign, OperationOption, HeaderOption, CellOption, PopUpComponent } from '../../shared';
import { LoginService } from '../../login';
import { SignService } from './sign.service';

@Component({
  selector: 'sign',
  templateUrl: './sign.component.html',
  styleUrls: ['./sign.component.css']
})
export class SignComponent implements OnInit {

  @ViewChild(PopUpComponent) popup: PopUpComponent;

  signs: Sign[];
  selectedCourseName = '';
  selectedDate: string;
  // list组件配置
  operationOption: OperationOption = { hasDelete: true, canClick: true };
  headerOptions: HeaderOption[] = [
    { name: '所属课程' },
    { name: '起始时间',   width: '130px' },
    { name: '终止时间',   width: '130px' },
    { name: '已签',       width: '70px' },
    { name: '总数',       width: '70px' },
    { name: '签到状态' }, 
    { name: '创建时间' }
  ];
  cellOptions: CellOption[] = [
    { prop: 'courseName',   default: '无' }, 
    { prop: 'startTime',    default: '无' }, 
    { prop: 'endTime',      default: '无' },
    { prop: 'beforeSignIn', default: 0,   mainStyles: { color: '#97CC00' }, }, 
    { prop: 'studentCount', default: 0 }, 
    { 
      prop: 'state',
      valueEnum: {
        '0': { value: '未开始' },
        '1': { value: '进行中', style: { color: '#97CC00' } },
        '2': { value: '已结束', style: { color: '#FF6C60' } }
      },
      default: '无'
    },
    { prop: 'createdAt',    default: '无' }
  ];
  // 查询流
  searchTerms = new Subject<string>();

  constructor(
    private _route: ActivatedRoute,
    private _router: Router,
    private _loginService: LoginService,
    private _signService: SignService) {}

  ngOnInit() {
    // 输入签到课程名搜索
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
            condition['courseName'] = term;
          }
          condition['teacherId'] = body.data[0]._id;
          return this._signService.search(condition, 'createdAt', -1);
        } else {
          return Observable.of(body);
        }
      })
      .subscribe(body => {
        if (+body.code == 200) {
          this.signs = body.data;
        } else {
          this.popup.show(body.msg);
        }
      });

    this._route.queryParams
      .subscribe(params => {
        this.selectedDate = params['date'];
      });
  }

  createSign() {
    this._router.navigate(['/home/sign/create/step1']);
  }

  searchSign(courseName: string) {
    this.searchTerms.next(courseName);
  }

  removeSign(sign: Sign) {
    if (!confirm('确定删除该签到？')) {
      return;
    }
    this._signService.remove(sign._id)
      .subscribe(body => {
        if (+body.code == 200) {
          this.popup.show('删除成功');
          var index = this.signs.indexOf(sign);
          this.signs.splice(index, 1);
          this.signs = this.signs.slice(0);
        } else {
          this.popup.show(body.msg);
        }
      })
  }

  toDetail(sign: Sign) {
    this._router.navigate(['/home/sign', sign._id, 'detail']);
  }

}