import { Injectable } from '@angular/core';
import { Observable } from 'rxjs';
import { environment } from '../../environments/environment';
import { HttpClient } from '@angular/common/http';
import { catchError } from 'rxjs/operators';
import { HandleError, HttpErrorHandler } from '../error.service';
import { Appointment } from './appointment';
import { CookieService } from 'ngx-cookie-service';
import { AvailableSlots } from 'app/booking/available-slots';
import { AppointmentUpdateRequest } from './appointment-update-request';

@Injectable()
export class AppointmentService {
  entityUrl = environment.REST_API_URL + 'appointment';

  private readonly handlerError: HandleError;

  constructor(
    private http: HttpClient,
    private httpErrorHandler: HttpErrorHandler,
    private cookieService: CookieService
  ) {
    this.handlerError = httpErrorHandler.createHandleError('AppointmentService');
  }

  getUserAppointments(): Observable<Appointment[]> {
    return this.http
      .get<Appointment[]>(this.entityUrl + '/user/' + Number(this.cookieService.get('userId')))
      .pipe(catchError(this.handlerError('getUserAppointments', [])));
  }

  getAppointmentById(appointmentId: number): Observable<Appointment> {
    return this.http
      .get<Appointment>(this.entityUrl + '/' + appointmentId)
      .pipe(catchError(this.handlerError('getAppointmentById', {} as Appointment)));
  }

  getAvailableSlotsForAppointment(selectedDate: string, appointmentId: number): Observable<AvailableSlots[]> {
    if (selectedDate == undefined) {
      selectedDate = new Date().toDateString();
    }

    let url = this.entityUrl + '/' + appointmentId + '/available-slots?date=' + selectedDate;

    return this.http
      .get<AvailableSlots[]>(url)
      .pipe(catchError(this.handlerError('getAvailableSlots', [])));
  }

  updateAppointment(appointmentId: string, appointment: AppointmentUpdateRequest): Observable<{}> {
    return this.http
      .put<AppointmentUpdateRequest>(this.entityUrl + '/' + appointmentId, appointment)
      .pipe(catchError(this.handlerError('updateAppointment', appointment)));
  }
}
