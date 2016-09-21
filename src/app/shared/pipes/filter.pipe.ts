import { Pipe, PipeTransform } from '@angular/core';

@Pipe({
  name: 'filter'
})
export class FilterPipe implements PipeTransform {

  transform(values: any[], filter: any, prop: string): any[] {
    if (!values) {
      return [];
    }
    if (!filter) {
      return values;
    }

    var results: any[] = values;
    results = values.filter((value: any) => {
      if (prop) {
        value = value[prop];
      }
      if (value == filter) {
        return true;
      }
      return false;
    });
    return results;
  }

}