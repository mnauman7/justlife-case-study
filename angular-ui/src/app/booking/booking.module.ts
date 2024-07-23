import {NgModule} from '@angular/core';
import {CommonModule} from '@angular/common';
import {FormsModule} from '@angular/forms';
import { BookingService } from './booking.service';
import { BookingRoutingModule } from './booking-routing.module';
import { BookingListComponent } from './booking-list/booking-list.component';

@NgModule({
  imports: [
    CommonModule,
    FormsModule,
    BookingRoutingModule
  ],
  declarations: [
    BookingListComponent
  ],
  providers: [BookingService]

})

export class BookingModule {
}
