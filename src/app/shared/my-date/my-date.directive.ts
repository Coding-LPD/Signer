import { Directive, Input, Output, EventEmitter } from '@angular/core';

@Directive({
  selector: 'input[type=datetime-local]',
  host: {
    '[value]': '_date',
    '(change)': 'onChange($event.target.value)'
  }
})
export class MyDateDirective {

  private _date: string;

  @Input() set date(d: string) {        
    this._date = d ? d.replace(' ', 'T') : '';
  }
  
  @Output() dateChange = new EventEmitter<string>();

  onChange(date: string) {    
    this.dateChange.emit(date.replace('T', ' '));
  }

}