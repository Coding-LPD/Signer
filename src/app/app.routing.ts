import { Routes, RouterModule } from '@angular/router';

import { LoginComponent, AuthGuardService } from './login';
import { RegisterComponent } from './register';
import { HomeComponent } from './home';
import { NoContent } from './no-content';
import { LogoComponent } from './logo';
import { ForgetComponent } from './forget';

const routes: Routes = [
  { path: '',           redirectTo: 'logo',      pathMatch: 'full' },
  { path: 'logo',       component: LogoComponent  },
  { path: 'login',      component: LoginComponent },
  { path: 'register',   component: RegisterComponent },
  { path: 'forget',     component: ForgetComponent },
  { path: '**',         component: NoContent }
];

export const appRoutingProviders: any[] = [
];

export const routing = RouterModule.forRoot(routes);