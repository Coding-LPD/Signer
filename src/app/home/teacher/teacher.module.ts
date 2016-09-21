import { NgModule } from '@angular/core';
import { HttpModule } from '@angular/http';

import { SharedModule } from '../../shared';
import { TeacherService } from './teacher.service';

@NgModule({
  imports: [
    SharedModule,
    HttpModule
  ],
  declarations: [

  ],
  exports: [

  ],
  providers: [
    TeacherService
  ]
})
export class TeacherModule {

}