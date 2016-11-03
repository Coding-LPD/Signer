import { 
  Component, Input, Output, EventEmitter, 
  OnInit, OnChanges, SimpleChange 
} from '@angular/core';

// 封装基本的数据类
interface WrapData {
  // 用户提供的数据
  data: any,
  // 数据当前选中状态
  checked: boolean
}

export interface OperationOption {
  // 是否要选择框
  hasCheck?: boolean,
  // 是否要编辑按钮
  hasEdit?: boolean,
  // 是否要删除按钮
  hasDelete?: boolean,
  // 数据行是否可以点击
  canClick?: boolean
}

// 表头配置
export interface HeaderOption {
  // 名字
  name: string;
  // 所占宽度
  width?: string;
}

// 内容单元格配置
export interface CellOption {
  // 提取该属性名对应的值  
  prop: string;
  // 单元格css样式
  mainStyles?: Object;
  // 根据获取到的值来决定显示的内容与样式  
  valueEnum?: {[prop: string]: ValueOption};
  // 默认值
  default?: any;
}

// 要显示的内容与样式配置
export interface ValueOption {
  value: any;
  style?: Object;
}

@Component({
  selector: 'list',
  templateUrl: './list.component.html',
  styleUrls: ['./list.component.css']
})
export class ListComponent implements OnChanges {
  
  @Input() operationOption: OperationOption = {};
  @Input() headerOptions: HeaderOption[] = [];
  @Input() cellOptions: CellOption[] = [];
  // 父组件提供的数据
  @Input() datas: any[] = [];
  // 点击复选框，数据选择事件
  @Output() onSelect = new EventEmitter<any[]>();
  // 编辑和删除事件
  @Output() onEdit = new EventEmitter<any>();
  @Output() onDelete = new EventEmitter<any>();
  // 点击某一行数据
  @Output() onRowClick = new EventEmitter<any>();
  // 封装父组件提供的数据
  allDatas: WrapData[];
  // 当前页显示的数据
  showDatas: WrapData[];
  // 是否选中当前页所有数据
  allSelected = false;

  selectePage(data: any[]) {
    this.showDatas = data;
    // 更改页面时，判断那页面所有数据选中状态
    this.allSelected = this.checkAllSelect(this.showDatas);
  }

  // 选中或者取消当前页面所有数据
  toggleSelectAll(datas: WrapData[]) {
    this.allSelected = !this.allSelected;
    datas.forEach((val: WrapData, index: number) => {
      val.checked = this.allSelected;
    });    
    if (this.allSelected) {
      this.onSelect.emit(this.showDatas);
    } else {
      this.onSelect.emit([]);
    }
  }

  toggleSelectOne(data: WrapData) {
    data.checked = !data.checked;    
    if (!data.checked) {      
      // 当前数据没选中，则说明没有选中当前页所有数据
      this.allSelected = false;
    } else {
      // 当前数据选中了，则判断当前页所有数据是否为选中
      this.allSelected = this.checkAllSelect(this.showDatas);
    }
    if (this.allSelected) {
      this.onSelect.emit(this.showDatas);
    } else {
      this.onSelect.emit(this.getCheckedData(this.showDatas));
    }
  }

  ngOnChanges(changes: {[prop: string]: SimpleChange}) {
    // 监测输入数据的变化，并将输入数据进行封装
    if (changes['datas'] && changes['datas'].currentValue) {
      this.allDatas = this.getWrapDatas(changes['datas'].currentValue);
    }
  }

  editData(data: any, $event: any) {
    $event.stopPropagation();
    this.onEdit.emit(data);
  }

  deleteData(data: any, $event: any) {
    $event.stopPropagation();
    this.onDelete.emit(data);
  } 

  clickRow(data: any) {
    if (this.operationOption.canClick) {
      this.onRowClick.emit(data); 
    }    
  }

  /**
   * 将普通数据封装成WrapData数据
   */
  private getWrapDatas(data: any[]) {
    var result: WrapData[] = [];
    data.forEach((val: any, index: number) => {
      result[index] = {
        data: val,
        checked: false
      }; 
    });
    return result;
  }

  /**
   * 查看指定的所有数据的选中状态
   */
  private checkAllSelect(data: WrapData[]) {
    if (data.length == 0) {
      return false;
    }
    return data.every((val: WrapData, index: number) => {
      if (!val.checked) {        
        return false;
      }
      return true;
    });
  }

  /**
   * 获取选中的数据
   */
  private getCheckedData(datas: WrapData[]) {
    var result: any[] = [];
    datas.forEach((val: WrapData, index: number) => {
      if (val.checked) {
        result.push(val.data);
      }
    });
    return result;
  }

}