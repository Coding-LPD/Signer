import { Component, Input } from '@angular/core';

@Component({
  selector: 'loading',
  templateUrl: './loading.component.html',
  styleUrls: ['./loading.component.css']
})
export class LoadingComponent {

  @Input('isShow') isShow = false;
  @Input('showLoading') showLoading = false;
  @Input('text') text = '加载完成';

}