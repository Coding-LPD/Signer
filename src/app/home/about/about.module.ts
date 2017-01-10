import { NgModule } from '@angular/core';

import { SharedModule } from '../../shared';
import { AboutComponent } from './about.component';

@NgModule({
  imports: [
    SharedModule
  ],
  declarations: [
    AboutComponent
  ],
  exports: [
    AboutComponent
  ]
})
export class AboutModule {

}