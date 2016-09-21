import { Routes, RouterModule } from '@angular/router';

import { LoginComponent, AuthGuardService } from './login';
import { RegisterComponent } from './register';
import { HomeComponent } from './home';
import { NoContent } from './no-content';

const routes: Routes = [
  { path: '',           redirectTo: 'login',      pathMatch: 'full' },
  { path: 'login',      component: LoginComponent },
  { path: 'register',   component: RegisterComponent },  
  { path: '**',         component: NoContent }
];

export const appRoutingProviders: any[] = [
];

export const routing = RouterModule.forRoot(routes);