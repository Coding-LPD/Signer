import { NgModule, ModuleWithProviders } from '@angular/core';
import { CommonModule } from '@angular/common';
import { RouterModule } from '@angular/router';
import { FormsModule } from '@angular/forms';

import { JsEncryptService } from './js-encrypt.service';
import { UserService } from './user.service';
import { API } from './api';
import { SmsCodeService, SmsCodeComponent } from './sms-code';
import { PopUpComponent } from './pop-up';
import { PaginationComponent } from './pagination';
import { ListComponent } from './list';
import { MyDateDirective } from './my-date';
import { FilterDatePipe, DistinctPipe, FilterPipe } from './pipes';

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
    FilterPipe
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
    FilterPipe
  ]
})
export class SharedModule { 

  static forRoot(): ModuleWithProviders {
    return {
      ngModule: SharedModule,
      providers: [JsEncryptService, SmsCodeService, UserService]
    };
  }

}