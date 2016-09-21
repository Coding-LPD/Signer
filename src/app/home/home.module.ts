import { NgModule, ModuleWithProviders } from '@angular/core';

import { SharedModule } from '../shared';
import { HomeComponent } from './home.component.ts';
import { HeaderComponent } from './header';
import { SidebarComponent } from './sidebar';
import { CalendarModule } from './calendar';
import { SignModule } from './sign';
import { homeRouting, homeRoutingProviders } from './home.routing';
import { StudentModule } from './student';
import { CourseModule } from './course';
import { TeacherModule } from './teacher';

@NgModule({
  imports: [
    SharedModule,
    SignModule,
    CalendarModule,
    StudentModule,
    CourseModule,
    TeacherModule,
    homeRouting  
  ],
  declarations: [
    HomeComponent,
    HeaderComponent,
    SidebarComponent,
  ],
  exports: [
    HomeComponent,
    HeaderComponent,
    SidebarComponent,
  ],
  providers: [
    homeRoutingProviders
  ]
})
export class HomeModule {

  static forRoot(): ModuleWithProviders {
    return {
      ngModule: HomeModule,
      providers: []
    };
  }

}