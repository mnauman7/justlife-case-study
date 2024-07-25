import {Component, OnInit} from '@angular/core';
import {Router} from '@angular/router';
import { finalize } from 'rxjs/operators';
import { Booking } from '../booking';
import { BookingService } from '../booking.service';
import { AvailableSlots } from '../available-slots';
import { AppointmentRequest } from '../appointment-request';
import { Staff } from '../staff';
import { CookieService } from 'ngx-cookie-service';

@Component({
  selector: 'app-booking-list',
  templateUrl: './booking-list.component.html',
  styleUrls: ['./booking-list.component.css']
})
export class BookingListComponent implements OnInit {
  errorMessage: string;
  isUsersDataReceived: boolean = false;

  availableSlots: AvailableSlots[];

  selectedDate: Date;
  //selectedTime: string;
  serviceHours: number;

  numberOfProfessionals: number;

  constructor(private router: Router, private bookingService: BookingService,
     private cookieService: CookieService) {
  }

  ngOnInit() {
  }

  bookAppointment(slot: AvailableSlots) {
    const appointmentRequest: AppointmentRequest = {
      userId: Number(this.cookieService.get('userId')),
      startingTimeId: slot.startingTimeId,
      duration: slot.duration,
      appointmentDate: slot.date,
      serviceTypeId: 1, //all bookings are cleaning service for now
      vehicleId: slot.staff1.vehicleId,
      address: "Test place",
      city: "Test City",
      requiredStaff: this.getSelectedStaff(slot)
    };
    
    this.bookingService.createAppointment(appointmentRequest).subscribe(
      newAppointment => {
        this.gotoAppointmentList();
      },
      error => this.errorMessage = error as any
    );
  }

  gotoAppointmentList() {
    this.router.navigate(['appointments']);
  }

  getSelectedStaff(slot: AvailableSlots) {
    if(this.numberOfProfessionals == 1){
      return [slot.staff1];
    }else if(this.numberOfProfessionals == 2){
      return [slot.staff1,slot.staff2];
    }else if(this.numberOfProfessionals == 3){
      return [slot.staff1,slot.staff2,slot.staff3];
    }
  }

  getAvailableSlots()
  {
      this.bookingService.getAvailableSlots(this.selectedDate,this.serviceHours,this.numberOfProfessionals)
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
