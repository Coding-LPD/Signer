import { NgModule, ModuleWithProviders } from '@angular/core';
import { CommonModule } from '@angular/common';
import { RouterModule } from '@angular/router';
import { FormsModule } from '@angular/forms';

import { JsEncryptService } from './js-encrypt.service';
import { UserService } from './user.service';
import { API } from './api';
import { MyDateDirective } from './my-date';
import { PositionService } from './position.service';
import { SignRecordService } from './sign-record.service';
import { SocketService } from './socket.service';
import { FilterDatePipe, DistinctPipe, FilterPipe } from './pipes';
import {   
  SmsCodeComponent, PopUpComponent, PaginationComponent, ListComponent,
  SmsCodeService, CircleProgressComponent
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
    CircleProgressComponent
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
    CircleProgressComponent
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