import { NgModule, ModuleWithProviders } from '@angular/core';
import { CommonModule } from '@angular/common';
import { RouterModule } from '@angular/router';
import { FormsModule } from '@angular/forms';

import { MyDateDirective } from './my-date';
import { FilterDatePipe, DistinctPipe, FilterPipe } from './pipes';
import { 
  API, UserService, JsEncryptService, PositionService,
  SignRecordService, SocketService
} from './services';
import {   
  SmsCodeComponent, PopUpComponent, PaginationComponent, ListComponent,
  SmsCodeService, CircleProgressComponent, BaiduMapComponent, LoadingComponent
} from './components';

@NgModule({
  imports: [
    CommonModule,
    FormsModule
  ],
  declarations: [
    SmsCodeComponent,
    PopUpComponent,
    PaginationComponent,
    ListComponent,
    MyDateDirective,
    FilterDatePipe,
    DistinctPipe,
    FilterPipe,
    CircleProgressComponent,
    BaiduMapComponent,
    LoadingComponent
  ],
  exports: [
    CommonModule,
    RouterModule,
    SmsCodeComponent,
    PopUpComponent,
    PaginationComponent,
    ListComponent,
    MyDateDirective,
    FilterDatePipe,
    DistinctPipe,
    FilterPipe,
    CircleProgressComponent,
    BaiduMapComponent,
    LoadingComponent
  ]
})
export class SharedModule { 

  static forRoot(): ModuleWithProviders {
    return {
      ngModule: SharedModule,
      providers: [
        JsEncryptService, SmsCodeService, UserService, 
        PositionService, SignRecordService, SocketService
      ]
    };
  }

}