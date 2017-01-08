import { Component, OnInit, Inject } from '@angular/core';
import { Router, NavigationExtras } from '@angular/router';
import { Observable } from 'rxjs/Observable';
import * as moment from 'moment';

import { LoginService } from '../../login';
import { TeacherService } from '../teacher';
import { CalendarPipe } from './calendar.pipe';

@Component({
  selector: 'calendar',
  templateUrl: './calendar.component.html',
  styleUrls: ['./calendar.component.css']
})
export class CalendarComponent implements OnInit {
  
  // 由于代码中要调用该管道，所以直接实例化
  @Inject(CalendarPipe) calendarPipe: CalendarPipe = new CalendarPipe();

  // 当我们选择同一月份的日期，则只改变日期的显示，不改变下方日历
  // 此时只强迫上方的日期纯管道执行，而不执行下方的日历纯管道，否则会降低执行速度
  // 日历上方显示的日期
  selectedDate = new Date();
  // 用于显示具体日历表
  calendarDate = new Date();
  // 视图中显示用的日历二维数组
  dates: Date[][] = [];
  // 对应日历二维数组的每日签到数据
  signs: any[][] = [];  

  constructor(
    private _router: Router,
    private _loginService: LoginService,
    private _teacherService: TeacherService  
  ) {}

  ngOnInit() {
    this.dates = this.calendarPipe.transform(this.calendarDate);
    this.getSigns();
  }

  subMonth() {
    this.calendarDate = new Date(this.calendarDate.getFullYear(), this.calendarDate.getMonth() - 1);    
    this.selectedDate = this.calendarDate;
    this.dates = this.calendarPipe.transform(this.calendarDate);
    this.getSigns();
  }

  addMonth() {
    this.calendarDate = new Date(this.calendarDate.getFullYear(), this.calendarDate.getMonth() + 1);
    this.selectedDate = this.calendarDate;
    this.dates = this.calendarPipe.transform(this.calendarDate);
    this.getSigns();
  }

  toToday() {
    this.calendarDate = new Date();
    this.selectedDate = this.calendarDate;
    this.dates = this.calendarPipe.transform(this.calendarDate);
    this.getSigns();
  }

  selectDate(date: Date) {
    if (date.getMonth() != this.calendarDate.getMonth()) {      
      this.calendarDate = new Date(this.calendarDate.getFullYear(), date.getMonth(), date.getDate());
      this.dates = this.calendarPipe.transform(this.calendarDate); 
      this.getSigns();     
    } else {
      this.calendarDate.setDate(date.getDate());
    }    
    this.selectedDate = new Date(date.getFullYear(), date.getMonth(), date.getDate());
  }

  setDayNumberClass(date: Date) {
    let classes =  {
      inactive: !moment(date).isSame(this.calendarDate, 'month'),
      selected: moment(date).isSame(this.calendarDate, 'day')
    };
    return classes;
  }

  setSignClass(date: Date) {
    let classes =  {
      inactive: !moment(date).isSame(this.calendarDate, 'month')
    };
    return classes;
  }

  createSign($event: any, date: Date) {
    $event.stopPropagation();
    var extra: NavigationExtras = { 
      queryParams: { 'date': moment(date).format('YYYY-MM-DD') }
    }    
    this._router.navigate(['/home/sign/create/step1'], extra);
  }

  toDaySignDetail($event: any, date: Date) {    
    $event.stopPropagation();
    var extra: NavigationExtras = { 
      queryParams: { 'date': moment(date).format('YYYY-MM-DD') }
    }    
    this._router.navigate(['/home/sign'], extra);
  }

  private getSigns() {
    this._loginService.getTeacherInfo()
      .flatMap(body => {
        if (+body.code == 200) {
          var teacherId = body.data[0]._id;
          var length1 = this.dates.length;
          var length2 = this.dates[length1 - 1].length;
          var startTime = moment(this.dates[0][0]).format('YYYY-MM-DD');
          var endTime = moment(this.dates[length1 - 1][length2 - 1]).format('YYYY-MM-DD');
          return this._teacherService.getSignsInPeriod(teacherId, startTime, endTime);
        } else {
          return Observable.of(body);
        }
      })
      .subscribe(body => {
        if (+body.code == 200) {
          // 同一天的签到按照开始时间倒序排序
          this.signs = body.data;
        } else {
          alert(body.msg);
        }
      });
  }

}