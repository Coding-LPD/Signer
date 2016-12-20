import { Routes } from '@angular/router';

import { StudentComponent } from './student.component';
import { CreateComponent } from './create';

export const studentRoutes: Routes = [
  { path: 'student',           component: StudentComponent },
  { path: 'student/create',    component: CreateComponent  },
  { path: 'student/edit/:id',  component: CreateComponent  }
];