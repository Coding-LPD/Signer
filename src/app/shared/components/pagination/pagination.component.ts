import { Component, Input, Output, EventEmitter, OnChanges, SimpleChange } from '@angular/core';

@Component({
  selector: 'pagination',
  templateUrl: './pagination.component.html',
  styleUrls: ['./pagination.component.css']
})
export class PaginationComponent implements OnChanges {
  
  @Input() data: any[];
  @Input() limit: number = 10;
  @Output() onSelectPage = new EventEmitter<any[]>();
  // 当前页
  currentPage = 1;
  // 总页面数
  pageCount = 1;
  // 用于生成相应数目的按钮
  pages: any[] = [1];

  toPage(page: number) {
    // 截取相应页的数据
    var start = this.limit * (page - 1);
    var result = this.data.slice(start, start + this.limit);
    // 更改当前页以及触发父组件方法
    this.currentPage = page;
    this.onSelectPage.emit(result);
  }

  ngOnChanges(changes: {[propKey: string]: SimpleChange}) {
    // 监控父组件对data属性的更改
    if (changes['data'] && changes['data'].currentValue) {
      // 重新计算页数，重置当前页，并跳转到第1页
      var newVal = changes['data'].currentValue;
      this.pageCount = Math.floor(newVal.length / this.limit);
      if (newVal % this.limit != 0) {
        this.pageCount++;
      }
      // 根据页面数来决定生成多少个按钮
      this.pages.length = this.pageCount == 0 ? 1 : this.pageCount;
      this.currentPage = 1;
      this.toPage(this.currentPage);
    }
  }

}