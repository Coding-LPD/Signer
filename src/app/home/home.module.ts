import { NgModule, ModuleWithProviders } from '@angular/core';
import { QRCodeModule } from 'angular2-qrcode';

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
import { StatisticsModule } from './statistics';
import { ChatModule } from './chat';
import { BigQRCodeComponent } from './big-qrcode';
import { AboutModule } from './about';

@NgModule({
  imports: [
    QRCodeModule,
    SharedModule,
    SignModule,
    CalendarModule,
    StudentModule,
    CourseModule,
    TeacherModule,
    StatisticsModule,
    ChatModule,
    AboutModule,
    homeRouting    
  ],
  declarations: [
    HomeComponent,
    HeaderComponent,
    SidebarComponent,
    BigQRCodeComponent
  ],
  exports: [
    HomeComponent,
    HeaderComponent,
    SidebarComponent,
    BigQRCodeComponent
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