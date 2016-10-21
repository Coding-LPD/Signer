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
  minIndex = 1;
  maxIndex = 11;
  // 用于生成option选项
  options = new Array(this.maxIndex);
  
  // 时间串格式为: "星期一 4节-8节"
  @Input() set time(t: string) {
    // 时间串为空
    if (!t.trim()) return;
    
    // 时间串格式不正确
    var times = t.split(' ');    
    if (times.length != 2) return;

    // 节数如果有负数，则split之后元素数会大于2
    var indexs = times[1].split('-');    
    if (indexs.length != 2) return;

    this.day = times[0];
    this.startIndex = Number(indexs[0].slice(0, -1));
    this.endIndex = Number(indexs[1].slice(0, -1));        
    this.checkIndex();
  }

  @Output() timeChange = new EventEmitter<string>();

  selectDay(day: string) {
    this.day = day; 
    this.timeChange.emit(this.getTimeString());
  }

  selectStartIndex(startIndex: number) {
    this.startIndex = startIndex;
    this.checkIndex();
    this.timeChange.emit(this.getTimeString());
  }

  selectEndIndex(endIndex: number) {
    this.endIndex = endIndex;
    this.checkIndex();
    this.timeChange.emit(this.getTimeString());
  }

  private getTimeString(): string {
    return `${this.day} ${this.startIndex}节-${this.endIndex}节`;
  }

  // 保证节数不会超过限定范围，并且起始节数不大于结束节数
  private checkIndex() {
    if (this.startIndex < this.minIndex || this.startIndex > this.maxIndex) {
      this.startIndex = this.minIndex;
    }
    if (this.endIndex < this.minIndex || this.endIndex > this.maxIndex) {
      this.endIndex = this.minIndex;
    }
    if (this.endIndex < this.startIndex) {
      var t = this.startIndex;
      this.startIndex = this.endIndex;
      this.endIndex = t;
    }
  }
}