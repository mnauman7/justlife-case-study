import {NgModule} from '@angular/core';
import {RouterModule, Routes} from '@angular/router';
import {PageNotFoundComponent} from './parts/page-not-found/page-not-found.component';
import {WelcomeComponent} from './parts/welcome/welcome.component';
import { LoginComponent } from './auth/login/login.component';
import { AuthenticationGuard } from './auth/auth-guard';

const appRoutes: Routes = [
  {path: '', component: LoginComponent, pathMatch: 'full'},
  {path: 'welcome', component: WelcomeComponent, canActivate: [AuthenticationGuard]},
  {path: '**', component: PageNotFoundComponent}
];

@NgModule({
  imports: [RouterModule.forRoot(appRoutes, {})],
  exports: [RouterModule]
})

export class AppRoutingModule {
}
