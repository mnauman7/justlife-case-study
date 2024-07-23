import {Component, OnInit} from '@angular/core';
import {Router} from '@angular/router';
import { finalize } from 'rxjs/operators';
import { Booking } from '../booking';
import { BookingService } from '../booking.service';
import { AvailableSlots } from '../available-slots';

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

  onSelect() {
    this.router.navigate(['/users']);
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
