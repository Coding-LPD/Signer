import { Pipe, PipeTransform } from '@angular/core';
import * as moment from 'moment';

@Pipe({
  name: 'filterDate'
})
export class FilterDatePipe implements PipeTransform {

    transform(values: any[], date: any, prop: string, granularity: string): any[] {
      if (!values) {
        return [];        
      } 
      if (!date) {
        return values;
      }

      granularity = granularity || 'day';      
      var results: any[] = values;
      var filterDate: Date;
      // 检测输入的日期是否合法，不合法则返回原数据
      if (date instanceof Date) {
        filterDate = date;
      } else if ((typeof date).toLowerCase() == 'string') {
        filterDate = new Date(date);
      }
      if (isNaN(filterDate.valueOf())) {
        console.error('argument "date" should be a valid date string or date instance');
        return results;
      }
      // 
      results = values.filter((value: any) => {
        // 不同日期或者无日期值，则过滤
        if (!value[prop]) {
          return false;
        }
        if (!moment(value[prop]).isSame(moment(filterDate), granularity)) {
          return false;
        }
        return true;
      });

      return results;
    }

}