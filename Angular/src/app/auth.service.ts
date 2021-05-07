import { Injectable } from '@angular/core';
import { User } from './interfaces/user';
import { HttpClient } from '@angular/common/http';
import { Observable, throwError } from 'rxjs';
import { catchError, retry } from 'rxjs/operators';
import { environment } from '../environments/environment';
import { map } from 'rxjs/operators';

@Injectable({
  providedIn: 'root',
})
export class AuthService {
  constructor(private http: HttpClient) {}

  currentUser: User;

  forgotPassword(username: string) {
    return this.http.get(environment.ApiUrl + `/${username}/forgot`);
  }
  resetPassword(username: string, password: string){
    return this.http.post(environment.ApiUrl + '/reset', {
      username,
      password,
    });
  }
  getCurrentUser() {
    return JSON.parse(localStorage.getItem('currentUser'));
  }

  setCurrentUser(user: User) {
    if (user == null) {
      localStorage.removeItem('currentUser');
      return;
    }
    localStorage.setItem('currentUser', JSON.stringify(user));
  }

  isLoggedIn() {
    if (localStorage.getItem('currentUser') != null) {
      return true;
    }
    return false;
  }

  login(username: string, password: string) {
    return this.http.post(environment.ApiUrl + '/login', {
      username,
      password,
    });
  }

  register(user: User) {
    return this.http.post(environment.ApiUrl + '/register', user);
  }
}
