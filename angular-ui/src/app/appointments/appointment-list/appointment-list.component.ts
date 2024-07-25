import {Component, OnInit} from '@angular/core';
import {Router} from '@angular/router';
import { finalize } from 'rxjs/operators';
import { Appointment } from '../appointment';
import { AppointmentService } from '../appointment.service';

@Component({
  selector: 'app-appointment-list',
  templateUrl: './appointment-list.component.html',
  styleUrls: ['./appointment-list.component.css']
})
export class AppointmentListComponent implements OnInit {
  errorMessage: string;
  searchValue: string;
  appointments: Appointment[];
  listOfAppointmentsWithLastName: Appointment[];
  isAppointmentsDataReceived: boolean = false;

  constructor(private router: Router, private appointmentService: AppointmentService) {

  }

  ngOnInit() {
    this.appointmentService.getUserAppointments().pipe(
      finalize(() => {
        this.isAppointmentsDataReceived = true;
      })
    ).subscribe(
      appointments => this.appointments = appointments,
      error => this.errorMessage = error as any);
  }

  onUpdate(appointment: Appointment) {
    this.router.navigate(['/appointments', appointment.appointmentId, 'edit']);
  }

}
