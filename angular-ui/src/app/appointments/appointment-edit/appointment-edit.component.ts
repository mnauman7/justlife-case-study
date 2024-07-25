import { Component, OnInit } from '@angular/core';
import { ActivatedRoute, Router } from '@angular/router';
import { Appointment } from '../appointment';
import { AppointmentService } from '../appointment.service';
import { AvailableSlots } from 'app/booking/available-slots';
import { AppointmentUpdateRequest } from '../appointment-update-request';

@Component({
  selector: 'app-appointment-edit',
  templateUrl: './appointment-edit.component.html',
  styleUrls: ['./appointment-edit.component.css'],
})
export class AppointmentEditComponent implements OnInit {
  appointment: Appointment;
  errorMessage: string; // server error message
  appointmentId: number;

  availableSlots: AvailableSlots[];
  selectedDate: string;
  selectedTime: number;

  constructor(
    private appointmentService: AppointmentService,
    private route: ActivatedRoute,
    private router: Router
  ) {
    this.appointment = {} as Appointment;
  }

  ngOnInit() {
    const appointmentId = this.route.snapshot.params.id;
    this.appointmentService.getAppointmentById(appointmentId).subscribe(
      (appointment) => (this.setAvailableSlots(appointment)),
      (error) => (this.errorMessage = error as any)
    );
  }

  onSubmit(appointment: Appointment) {
    const that = this;  
    const appointmentId = this.route.snapshot.params.id;

    const appointmentRequest: AppointmentUpdateRequest = {
      startingTimeId: this.selectedTime,
      appointmentDate: this.selectedDate,
    };

    this.appointmentService.updateAppointment(appointmentId , appointmentRequest).subscribe(
      (res) => this.goToAppointmentList(),
      (error) => (this.errorMessage = error as any)
    );
  }

  goToAppointmentList() {
    this.errorMessage = null;
    this.router.navigate(['/appointments']);
  }

  setAvailableSlots(appointment: Appointment){
    this.appointment = appointment;
    this.selectedDate = appointment.appointmentDate;
    this.setAvailableSlotsForTheDate();
  }

  setAvailableSlotsForTheDate(){
    this.appointmentService.getAvailableSlotsForAppointment(this.selectedDate,this.appointment.appointmentId)
    .subscribe(
    (availableSlots) => {

     this.availableSlots = availableSlots;

     },
     (error) =>
     {
       this.availableSlots = null;
     }
    );
  }
}
