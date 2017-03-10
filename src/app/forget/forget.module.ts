import { NgModule, ModuleWithProviders } from '@angular/core';
import { FormsModule } from '@angular/forms';
import { HttpModule } from '@angular/http';

import { SharedModule } from '../shared';
import { ForgetComponent } from './forget.component';
import { ForgetService } from './forget.service';

@NgModule({
  imports: [
    SharedModule,
    FormsModule,
    HttpModule
  ],
  declarations: [
    ForgetComponent
  ],
  exports: [
    ForgetComponent
  ]
})
export class ForgetModule {

  static forRoot(): ModuleWithProviders {
    return {
      ngModule: ForgetModule,
      providers: [ForgetService]
    };
  }

 }