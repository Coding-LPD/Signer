import { NgModule } from '@angular/core';

import { SharedModule } from '../../shared';
import { CalendarComponent } from './calendar.component';
import { CalendarPipe } from './calendar.pipe';

@NgModule({
  imports: [
    SharedModule
  ],
  declarations: [
    CalendarComponent,
    CalendarPipe
  ],
  exports: [
    CalendarComponent,
    CalendarPipe
  ]
})
export class CalendarModule {}