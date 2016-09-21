import { NgModule, ModuleWithProviders } from '@angular/core';
import { FormsModule } from '@angular/forms';
import { HttpModule } from '@angular/http';

import { SharedModule } from '../shared';
import { LoginService } from './login.service';
import { LoginComponent } from './login.component';

@NgModule({
  imports: [
    SharedModule,
    FormsModule,
    HttpModule
  ],
  declarations: [
    LoginComponent
  ],
  exports: [
    LoginComponent
  ]
})
export class LoginModule { 

  static forRoot(): ModuleWithProviders {
    return {
      ngModule: LoginModule,
      providers: [LoginService]
    }
  }

}