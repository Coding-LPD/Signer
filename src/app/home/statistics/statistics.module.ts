import { NgModule } from '@angular/core';
import { ChartsModule } from 'ng2-charts/ng2-charts';

import { SharedModule } from '../../shared';
import { StatisticsComponent } from './statistics.component';
import { LatestComponent } from './latest';
import { AllComponent } from './all';
import { StatisticsService } from './statistics.service';

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
  ],
  providers: [
    StatisticsService
  ]
})
export class StatisticsModule {

}