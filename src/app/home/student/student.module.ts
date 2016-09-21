import { NgModule } from '@angular/core';
import { FormsModule } from '@angular/forms';
import { HttpModule } from '@angular/http';
import { UPLOAD_DIRECTIVES } from 'ng2-uploader';

import { SharedModule } from '../../shared';
import { StudentComponent } from './student.component';
import { StudentService } from './student.service';

@NgModule({
  imports: [
    SharedModule,
    FormsModule,    
    HttpModule
  ],
  declarations: [
    StudentComponent,
    UPLOAD_DIRECTIVES
  ],
  exports: [
    StudentComponent
  ],
  providers: [
    StudentService
  ]
})
export class StudentModule {

}