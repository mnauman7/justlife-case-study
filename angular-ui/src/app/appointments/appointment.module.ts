import {NgModule} from '@angular/core';
import {CommonModule} from '@angular/common';
import {FormsModule} from '@angular/forms';
import { AppointmentListComponent } from './appointment-list/appointment-list.component';
import { AppointmentEditComponent } from './appointment-edit/appointment-edit.component';
import { AppointmentService } from './appointment.service';
import { AppointmentRoutingModule } from './appointment-routing.module';

@NgModule({
  imports: [
    CommonModule,
    FormsModule,
    AppointmentRoutingModule
  ],
  declarations: [
    AppointmentListComponent,
    AppointmentEditComponent
  ],
  providers: [AppointmentService]

})

export class AppointmentsModule {
}
