import { Pipe, PipeTransform } from '@angular/core';

// 在一组数据里面，抽取出指定属性所能得到的所有值
@Pipe({
  name: 'distinct'
})
export class DistinctPipe implements PipeTransform {

  transform(values: any[], prop: string): any[] {
    if (!values) {
      return [];
    }

    var results: any[] = [];
    var temp = {};
    values.forEach((value: any) => {
      if (!temp[value[prop]]) {
        temp[value[prop]] = true;
        results.push(value[prop]);
      }
    });
    return results;
  }

}