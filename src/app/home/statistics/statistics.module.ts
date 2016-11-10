import { NgModule } from '@angular/core';

import { StatisticsComponent } from './statistics.component';
import { LatestComponent } from './latest';
import { AllComponent } from './all';
import { SharedModule } from '../../shared'; 

@NgModule({
  imports: [
    SharedModule
  ],
  declarations: [
    StatisticsComponent,
    LatestComponent,
    AllComponent
  ],
  exports: [
    StatisticsComponent,
    LatestComponent,
    AllComponent    
  ]
})
export class StatisticsModule {

}