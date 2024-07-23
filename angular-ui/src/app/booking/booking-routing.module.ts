import {NgModule} from '@angular/core';
import {RouterModule, Routes} from '@angular/router';
import { BookingListComponent } from './booking-list/booking-list.component';

const bookingRoutes: Routes = [
  {path: 'booking', component: BookingListComponent}
];

@NgModule({
  imports: [RouterModule.forChild(bookingRoutes)],
  exports: [RouterModule]
})

export class BookingRoutingModule {
}
