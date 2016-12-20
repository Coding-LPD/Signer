import { NgModule } from '@angular/core';
import { FormsModule } from '@angular/forms';
import { HttpModule } from '@angular/http';
import { Ng2UploaderModule } from 'ng2-uploader';

import { SharedModule } from '../../shared';
import { StudentComponent } from './student.component';
import { StudentService } from './student.service';
import { CreateComponent } from './create';

@NgModule({
  imports: [
    SharedModule,
    FormsModule,    
    HttpModule,
    Ng2UploaderModule
  ],
  declarations: [
    StudentComponent,
    CreateComponent 
  ],
  exports: [
    StudentComponent,
    CreateComponent
  ],
  providers: [
    StudentService
  ]
})
export class StudentModule {

}