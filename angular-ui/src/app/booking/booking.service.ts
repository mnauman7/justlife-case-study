import { Injectable } from '@angular/core';
import { Booking } from './booking';
import { Observable } from 'rxjs';
import { environment } from '../../environments/environment';
import { HttpClient } from '@angular/common/http';
import { catchError } from 'rxjs/operators';
import { HandleError, HttpErrorHandler } from '../error.service';
import { AvailableSlots } from './available-slots';

@Injectable()
export class BookingService {
  entityUrl = environment.REST_API_URL + 'booking';

  private readonly handlerError: HandleError;

  constructor(
    private http: HttpClient,
    private httpErrorHandler: HttpErrorHandler
  ) {
    this.handlerError = httpErrorHandler.createHandleError('UserService');
  }

  getUsers(): Observable<Booking[]> {
    return this.http
      .get<Booking[]>(this.entityUrl)
      .pipe(catchError(this.handlerError('getUsers', [])));
  }

  getUserById(userId: number): Observable<Booking> {
    return this.http
      .get<Booking>(this.entityUrl + '/' + userId)
      .pipe(catchError(this.handlerError('getUserById', {} as Booking)));
  }

  getUserDetails(userId: number): Observable<Booking> {
    return this.http
      .get<Booking>(this.entityUrl + '/' + userId + '/details')
      .pipe(catchError(this.handlerError('getUserById', {} as Booking)));
  }

  addUser(user: Booking): Observable<Booking> {
    return this.http
      .post<Booking>(this.entityUrl, user)
      .pipe(catchError(this.handlerError('addUser', user)));
  }


  updateUser(userId: string, user: Booking): Observable<{}> {
    return this.http
      .put<Booking>(this.entityUrl + '/' + userId, user)
      .pipe(catchError(this.handlerError('updateUser', user)));
  }

  deleteUser(userId: string): Observable<{}> {
    return this.http
      .delete<Booking>(this.entityUrl + '/' + userId)
      .pipe(catchError(this.handlerError('deleteUser', [userId])));
  }

  updateUserActiveStatus(userId: string, isActive: boolean): Observable<{}> {
    return this.http
      .put<Booking>(this.entityUrl + '/' + userId + '/active?isActive=' + isActive, null)
      .pipe(catchError(this.handlerError('updateUserActiveStatus')));
  }

  getAvailableSlots(selectedDate: Date, serviceHours: number, professionals: number): Observable<AvailableSlots[]> {
    if (selectedDate == undefined) {
      selectedDate = new Date();
    }

    let url = this.entityUrl + '/available-slots?date=' + selectedDate;
    
    if (serviceHours !== undefined) {
      url += '&service-hours=' + serviceHours;
    }
    if (professionals !== undefined) {
      url += '&professionals=' + professionals;
    }
    return this.http
      .get<AvailableSlots[]>(url)
      .pipe(catchError(this.handlerError('getAvailableSlots', [])));
  }
}
