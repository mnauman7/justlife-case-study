import { Injectable } from '@angular/core';
import { User } from './user';
import { Observable } from 'rxjs';
import { environment } from '../../environments/environment';
import { HttpClient } from '@angular/common/http';
import { catchError } from 'rxjs/operators';
import { HandleError, HttpErrorHandler } from '../error.service';

@Injectable()
export class UserService {
  entityUrl = environment.REST_API_URL + 'user';

  private readonly handlerError: HandleError;

  constructor(
    private http: HttpClient,
    private httpErrorHandler: HttpErrorHandler
  ) {
    this.handlerError = httpErrorHandler.createHandleError('UserService');
  }

  getUsers(): Observable<User[]> {
    return this.http
      .get<User[]>(this.entityUrl)
      .pipe(catchError(this.handlerError('getUsers', [])));
  }

  getUserById(userId: number): Observable<User> {
    return this.http
      .get<User>(this.entityUrl + '/' + userId)
      .pipe(catchError(this.handlerError('getUserById', {} as User)));
  }

  getUserDetails(userId: number): Observable<User> {
    return this.http
      .get<User>(this.entityUrl + '/' + userId + '/details')
      .pipe(catchError(this.handlerError('getUserById', {} as User)));
  }

  addUser(user: User): Observable<User> {
    return this.http
      .post<User>(this.entityUrl, user)
      .pipe(catchError(this.handlerError('addUser', user)));
  }


  updateUser(userId: string, user: User): Observable<{}> {
    return this.http
      .put<User>(this.entityUrl + '/' + userId, user)
      .pipe(catchError(this.handlerError('updateUser', user)));
  }

  deleteUser(userId: string): Observable<{}> {
    return this.http
      .delete<User>(this.entityUrl + '/' + userId)
      .pipe(catchError(this.handlerError('deleteUser', [userId])));
  }

  updateUserActiveStatus(userId: string, isActive: boolean): Observable<{}> {
    return this.http
      .put<User>(this.entityUrl + '/' + userId + '/active?isActive=' + isActive, null)
      .pipe(catchError(this.handlerError('updateUserActiveStatus')));
  }

  searchUsers(searchValue: string): Observable<User[]> {
    let url = this.entityUrl;
    if (searchValue !== undefined) {
      url += '?search=' + searchValue;
    }
    return this.http
      .get<User[]>(url)
      .pipe(catchError(this.handlerError('searchUsers', [])));
  }
}
