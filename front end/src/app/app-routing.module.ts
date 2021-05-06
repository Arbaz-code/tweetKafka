import { NgModule } from '@angular/core';
import { RouterModule, Routes } from '@angular/router';
import { LoginComponent } from './login/login.component';
import { RegisterComponent } from './register/register.component';
import { ResetPassComponent } from './reset/reset-pass/reset-pass.component';
import { TweetListComponent } from './tweet-list/tweet-list.component';
import { UserListComponent } from './user-list/user-list.component';

const routes: Routes = [
  { path: 'login', component: LoginComponent },
  { path: 'register', component: RegisterComponent },
  { path: 'home', component: TweetListComponent },
  { path: 'user-list', component: UserListComponent },
  { path: 'tweets/:username', component: TweetListComponent },
  { path: 'passwordReset/:username', component: ResetPassComponent },
  { path: '', redirectTo: '/login', pathMatch: 'full' },
];

@NgModule({
  imports: [RouterModule.forRoot(routes)],
  exports: [RouterModule],
})
export class AppRoutingModule {}
