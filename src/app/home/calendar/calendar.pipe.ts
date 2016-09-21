import { Pipe, PipeTransform } from '@angular/core';

// 根据当前日期，返回该月份的日历表
@Pipe({
  name: 'calendarMonth'
})
export class CalendarPipe implements PipeTransform {  

  transform(value: Date): Date[][] {
    var result: Date[][] = [];
    var year = value.getFullYear();
    var month = value.getMonth();
    // 从该月第一天开始，依次填满日历二维数组
    var date = new Date(year, month, 1);
    var days = this.getDaysInOneMonth(year, month);
    var row = 0;
    for (var i=1; i<=days; i++) {
      date.setDate(i);      
      var col = date.getDay();
      // 填满一周，则换下一周
      if (col % 7 == 0) {
        row++;
      }
      // 遇到未初始化的数组，则先初始化
      if (!result[row]) {
        result[row] = new Array<Date>(7);
      }
      result[row][col] = new Date(year, month, i);
    }    
    result = this.fixCalendarEnds(value, result);
    return result;
  }

  getDaysInOneMonth(year: number, month: number){      
    var d = new Date(year, month + 1, 0);  
    return d.getDate();  
  }  

  /**
   * 将日历二维数组的前后填补上其他月份的日期
   */
  fixCalendarEnds(date: Date, calendar: Date[][]) {
    var year = date.getFullYear();
    var month = date.getMonth();
    var i: number;

    // 补齐第一周空白的日期
    var lastIndex: number;
    // 找到本月第一周的最后一个空缺日期索引
    for (i=calendar[0].length-1; i>=0; i--) {
      if (!calendar[0][i]) {
        lastIndex = i;
        break;
      }
    }
    // 在本月第一周依次填上上个月的日期
    var lastMonthDays = this.getDaysInOneMonth(year, month - 1);
    for (i=lastIndex; i>=0; i--) {
      calendar[0][i] = new Date(year, month-1, lastMonthDays--);
    }

    // 补齐最后一周空白的日期
    var length = calendar.length;
    var firstIndex: number;
    // 找到本月最后一周的第一个空缺日期索引
    for (i=0; i<calendar[0].length; i++) {
      if (!calendar[length-1][i]) {
        firstIndex = i;
        break;
      }
    }
    // 在本月最后一周依次填上下个月的日期
    var nextMonthDays = 1;
    for (i=firstIndex; i<calendar[0].length; i++) {
      calendar[length-1][i] = new Date(year, month+1, nextMonthDays++);
    }

    return calendar;
  }

}