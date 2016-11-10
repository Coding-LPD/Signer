import { Routes, RouterModule } from '@angular/router';

import { HomeComponent } from './home.component';
import { CalendarComponent } from './calendar';
import { AuthGuardService } from '../login';
import { signRoutes } from './sign';
import { StudentComponent } from './student';
import { courseRoutes } from './course';
import { statisticsRoutes } from './statistics';

const routes: Routes = [
  { 
    path: 'home', 
    component: HomeComponent,
    canActivate: [AuthGuardService],
    children: [
      { path: '',           redirectTo: 'calendar',       pathMatch: 'full'   },
      { path: 'calendar',   component: CalendarComponent                      },
      { path: 'student',    component: StudentComponent                       },
      { path: 'chatRoom',   component: CalendarComponent                      },
      { path: 'about',      component: CalendarComponent                      },
      ...signRoutes,
      ...courseRoutes,
      ...statisticsRoutes
    ]
  }
];

export const homeRoutingProviders: any[] = [
  AuthGuardService
];

export const homeRouting = RouterModule.forChild(routes);