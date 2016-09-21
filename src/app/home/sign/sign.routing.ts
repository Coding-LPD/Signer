import { Routes } from '@angular/router';

import { SignComponent } from './sign.component';
import { createRoutes } from './create';

export const signRoutes: Routes = [
  { path: 'sign',             component: SignComponent     },
  ...createRoutes
];