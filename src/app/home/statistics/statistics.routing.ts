import { Routes, RouterModule } from '@angular/router';

import { StatisticsComponent } from './statistics.component';
import { LatestComponent } from './latest';
import { AllComponent } from './all';

export const statisticsRoutes: Routes = [
  {
    path: 'statistics',
    component: StatisticsComponent,
    children: [
      { path: '',         redirectTo: 'latest',       pathMatch: 'full'   },
      { path: 'latest',   component: LatestComponent                      },
      { path: 'all',      component: AllComponent                         }
    ]
  }
];