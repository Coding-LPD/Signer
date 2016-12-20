import { Routes } from '@angular/router';

import { CourseComponent } from './course.component';
import { CreateComponent } from './create';

export const courseRoutes: Routes = [
  { path: 'course',           component: CourseComponent },
  { path: 'course/create',    component: CreateComponent },
  { path: 'course/edit/:id',  component: CreateComponent }
];