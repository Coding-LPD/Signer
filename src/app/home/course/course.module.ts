import { NgModule } from '@angular/core';
import { FormsModule } from '@angular/forms';
import { HttpModule } from '@angular/http';

import { SharedModule } from '../../shared';
import { CourseComponent } from './course.component';
import { CourseService } from './course.service';
import { CreateComponent } from './create';

@NgModule({
  imports: [
    SharedModule,
    FormsModule,    
    HttpModule
  ],
  declarations: [
    CourseComponent,
    CreateComponent
  ],
  exports: [
    CourseComponent,
    CreateComponent
  ],
  providers: [
    CourseService
  ]
})
export class CourseModule {

}