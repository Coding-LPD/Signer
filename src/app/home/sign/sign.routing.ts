import { Routes } from '@angular/router';

import { SignComponent } from './sign.component';
import { createRoutes } from './create';
import { DetailComponent } from './detail';

export const signRoutes: Routes = [
  { path: 'sign',             component: SignComponent     },
  { path: 'sign/detail/:id',      component: DetailComponent   },
  ...createRoutes  
];