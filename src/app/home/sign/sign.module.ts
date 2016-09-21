import { NgModule } from '@angular/core';
import { FormsModule } from '@angular/forms';
import { QRCodeModule } from 'angular2-qrcode';

import { SharedModule } from '../../shared';
import { SignComponent } from './sign.component';
import { SignService } from './sign.service';
import { DetailComponent } from './detail';
import { 
  CreateComponent, 
  Step1Component, 
  Step2Component, 
  Step3Component, 
  CompleteComponent 
} from './create';

@NgModule({
  imports: [
    SharedModule,
    QRCodeModule,
    FormsModule
  ],
  declarations: [
    SignComponent,
    CreateComponent,
    Step1Component,
    Step2Component,
    Step3Component,
    CompleteComponent,
    DetailComponent    
  ],
  exports: [
    SignComponent,
    CreateComponent,
    Step1Component,
    Step2Component,
    Step3Component,
    CompleteComponent,
    DetailComponent
  ],
  providers: [
    SignService
  ]
})
export class SignModule {

}