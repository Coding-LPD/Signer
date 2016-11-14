import { Routes } from '@angular/router';

import { SignComponent } from './sign.component';
import { createRoutes } from './create';
import { DetailComponent } from './detail';
import { AddSignInComponent } from './add-sign-in';

export const signRoutes: Routes = [
  { path: 'sign',                   component: SignComponent      },
  { path: 'sign/:id/detail',        component: DetailComponent    },
  { path: 'sign/:id/record/add',    component: AddSignInComponent },
  ...createRoutes  
];