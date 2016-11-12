import { NgModule } from '@angular/core';
import { ChartsModule } from 'ng2-charts/ng2-charts';

import { StatisticsComponent } from './statistics.component';
import { LatestComponent } from './latest';
import { AllComponent } from './all';
import { SharedModule } from '../../shared'; 

@NgModule({
  imports: [
    SharedModule,
    ChartsModule
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