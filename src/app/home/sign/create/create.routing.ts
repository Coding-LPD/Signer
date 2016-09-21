import { Routes, RouterModule } from '@angular/router';

import { CreateComponent } from './create.component';
import { Step1Component } from './step1';
import { Step2Component } from './step2';
import { Step3Component } from './step3';
import { CompleteComponent } from './complete';

export const createRoutes: Routes = [
  { 
    path: 'sign/create', 
    component: CreateComponent,
    children: [
      { path: 'step1',      component: Step1Component     },
      { path: 'step2',      component: Step2Component     },
      { path: 'step3',      component: Step3Component     },
      { path: 'complete',   component: CompleteComponent  }
    ]
  }
];