import { Injectable } from '@angular/core';
import { Observable } from 'rxjs';
import { environment } from '../../environments/environment';
import { HttpClient } from '@angular/common/http';
import { catchError } from 'rxjs/operators';
import { HandleError, HttpErrorHandler } from '../error.service';
import { LoginCreds } from './login/login-creds';

@Injectable()
export class AuthService {
  entityUrl = environment.REST_API_URL + 'auth';

  isAuthenticated: Boolean = false;

  private readonly handlerError: HandleError;

  constructor(
    private http: HttpClient,
    private httpErrorHandler: HttpErrorHandler
  ) {
    this.handlerError = httpErrorHandler.createHandleError('AuthService');
  }

  loginUser(loginCreds: LoginCreds): Observable<any> {
    return this.http
      .post<any>(this.entityUrl + '/login', loginCreds)
      .pipe(catchError(this.handlerError('loginUser', loginCreds)));
  }

  // updateUserActiveStatus(userId: string, isActive: boolean): Observable<{}> {
  //   return this.http
  //     .put<User>(this.entityUrl + '/' + userId + '/active?isActive=' + isActive, null)
  //     .pipe(catchError(this.handlerError('updateUserActiveStatus')));
  // }

}
