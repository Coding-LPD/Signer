import { Component } from '@angular/core';
import { Router } from '@angular/router';
import * as moment from 'moment';

@Component({
  selector: 'calendar',
  templateUrl: './calendar.component.html',
  styleUrls: ['./calendar.component.css']
})
export class CalendarComponent {

  // 当我们选择同一月份的日期，则只改变日期的显示，不改变下方日历
  // 此时只强迫上方的日期纯管道执行，而不执行下方的日历纯管道，否则会降低执行速度
  // 日历上方显示的日期
  selectedDate = new Date();
  // 用于显示具体日历表
  calendarDate = new Date();

  constructor(private _router: Router) {}

  subMonth() {
    this.calendarDate = new Date(this.calendarDate.getFullYear(), this.calendarDate.getMonth() - 1);
    this.selectedDate = this.calendarDate;
  }

  addMonth() {
    this.calendarDate = new Date(this.calendarDate.getFullYear(), this.calendarDate.getMonth() + 1);
    this.selectedDate = this.calendarDate;
  }

  toToday() {
    this.calendarDate = new Date();
    this.selectedDate = this.calendarDate;
  }

  selectDate(date: Date) {
    if (date.getMonth() != this.calendarDate.getMonth()) {      
      this.calendarDate = new Date(this.calendarDate.getFullYear(), date.getMonth(), date.getDate());      
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

  createSign($event: any) {
    $event.stopPropagation();
    this._router.navigate(['/home/sign/create/step1']);
  }

  toDaySignDetail($event: any) {
    $event.stopPropagation();
  }

}