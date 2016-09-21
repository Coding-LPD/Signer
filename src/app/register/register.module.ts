import { NgModule, ModuleWithProviders } from '@angular/core';
import { FormsModule } from '@angular/forms';
import { HttpModule } from '@angular/http';

import { SharedModule } from '../shared';
import { RegisterComponent } from './register.component';
import { RegisterService } from './register.service';

@NgModule({
  imports: [
    SharedModule,
    FormsModule,
    HttpModule
  ],
  declarations: [
    RegisterComponent
  ],
  exports: [
    RegisterComponent
  ]
})
export class RegisterModule {

  static forRoot(): ModuleWithProviders {
    return {
      ngModule: RegisterModule,
      providers: [RegisterService]
    };
  }

 }