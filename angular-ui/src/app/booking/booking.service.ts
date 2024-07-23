import { Injectable } from '@angular/core';
import { Booking } from './booking';
import { Observable } from 'rxjs';
import { environment } from '../../environments/environment';
import { HttpClient } from '@angular/common/http';
import { catchError } from 'rxjs/operators';
import { HandleError, HttpErrorHandler } from '../error.service';
import { AvailableSlots } from './available-slots';
import { AppointmentRequest } from './appointment-request';

@Injectable()
export class BookingService {
  entityUrl = environment.REST_API_URL + 'appointment';

  private readonly handlerError: HandleError;

  constructor(
    private http: HttpClient,
    private httpErrorHandler: HttpErrorHandler
  ) {
    this.handlerError = httpErrorHandler.createHandleError('BookingService');
  }


  createAppointment(appointment: AppointmentRequest): Observable<{}> {
    return this.http
      .post<AppointmentRequest>(this.entityUrl, appointment)
      .pipe(catchError(this.handlerError('createAppointment', appointment)));
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
