import { NgModule } from '@angular/core';
import { BrowserModule } from '@angular/platform-browser';
import { RequestOptions } from '@angular/http';

import { SharedModule } from './shared';
import { routing, appRoutingProviders } from './app.routing';
import { AppComponent } from './app.component';
import { LoginModule } from './login';
import { RegisterModule } from './register';
import { HomeModule, HomeComponent } from './home';
import { NoContent } from './no-content';
import { LogoComponent } from './logo';
import { ForgetModule } from './forget';

@NgModule({
  imports: [
    BrowserModule,
    routing,    
    SharedModule.forRoot(),
    LoginModule.forRoot(),
    RegisterModule.forRoot(),
    HomeModule.forRoot(),
    ForgetModule.forRoot()
  ],  
  declarations: [
    AppComponent,    
    NoContent,
    LogoComponent
  ],
  exports: [
    LogoComponent
  ],
  providers: [
    appRoutingProviders
  ],
  bootstrap: [ AppComponent ]
})
export class AppModule { }