import {Component, OnInit} from '@angular/core';
import {Router} from '@angular/router';
import { finalize } from 'rxjs/operators';
import { Booking } from '../booking';
import { BookingService } from '../booking.service';
import { AvailableSlots } from '../available-slots';
import { AppointmentRequest } from '../appointment-request';

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
  serviceHours: number;
  numberOfProfessionals: number;

  constructor(private router: Router, private bookingService: BookingService) {
  }

  ngOnInit() {
  }

  bookAppointment(slot: AvailableSlots) {
    const appointmentRequest: AppointmentRequest = {
      userId: 1,
      startingTimeId: slot.startingTimeId,
      duration: slot.duration,
      appointmentDate: slot.date,
      serviceTypeId: 1,
      vehicleId: 1,
      address: "Test place",
      city: "Test City",
      requiredStaff: slot.availableStaff.slice(0,2)
    };
    
    this.bookingService.createAppointment(appointmentRequest).subscribe(
      newAppointment => {
        this.gotoAppointmentList();
      },
      error => this.errorMessage = error as any
    );
  }

  gotoAppointmentList() {
    this.router.navigate(['welcome']);
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
