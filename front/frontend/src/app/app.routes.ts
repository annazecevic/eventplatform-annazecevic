import { Routes } from '@angular/router';
import { Login } from './pages/login/login';
import { Register } from './pages/register/register';
import { AdminComponent } from './pages/admin/admin';
import {ProfileComponent} from './pages/profile/profile';
import {Homepage} from './pages/homepage/homepage';
import {Locations} from './pages/locations/locations';

export const routes: Routes = [
  { path: '', redirectTo: 'login', pathMatch: 'full' },
  { path: 'login', component: Login },
  { path: 'register', component: Register },
  { path: 'admin', component: AdminComponent },
  { path: 'profile', component: ProfileComponent },
  { path: 'homepage', component: Homepage },
  { path: 'locations', component: Locations },

  { path: '**', redirectTo: 'login' }
];
