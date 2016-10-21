import { Component, Input, Output, EventEmitter } from '@angular/core';

@Component({
  selector: 'class-time',
  templateUrl: './class-time.component.html',
  styleUrls: ['./class-time.component.css']
})
export class ClassTimeComponent {

  day = '星期一';
  startIndex = 1;
  endIndex = 1;
  
  @Input() set time(t: string) {
    if (!t.trim()) return;
    var times = t.split(' ');
    var indexs = times[1].split('-');   
    this.day = times[0];    
    this.startIndex = Number(indexs[0].slice(0, 1));
    this.endIndex = Number(indexs[1].slice(0, 1));
  }

  @Output() timeChange = new EventEmitter<string>();

  selectDay(day: string) {
    this.day = day; 
    this.timeChange.emit(this.getTimeString());
  }

  selectStartIndex(startIndex: number) {
    // 保证开始节数小于结束节数
    if (startIndex <= this.endIndex) {
      this.startIndex = startIndex;
    } else {
      this.startIndex = this.endIndex;
      this.endIndex = startIndex;
    }
    this.timeChange.emit(this.getTimeString());
  }

  selectEndIndex(endIndex: number) {
    // 保证开始节数小于结束节数
    if (this.startIndex <= endIndex) {
      this.endIndex = endIndex;
    } else {
      this.endIndex = this.startIndex;
      this.startIndex = endIndex;
    }
    this.timeChange.emit(this.getTimeString());
  }

  getTimeString(): string {
    return `${this.day} ${this.startIndex}节-${this.endIndex}节`;
  }
}