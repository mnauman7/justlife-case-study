import {NgModule} from '@angular/core';
import {RouterModule, Routes} from '@angular/router';
import { AppointmentListComponent } from './appointment-list/appointment-list.component';
import { AppointmentEditComponent } from './appointment-edit/appointment-edit.component';

const appointmentRoutes: Routes = [
  {path: 'appointments', component: AppointmentListComponent},
  {path: 'appointments/:id/edit', component: AppointmentEditComponent}
];

@NgModule({
  imports: [RouterModule.forChild(appointmentRoutes)],
  exports: [RouterModule]
})

export class AppointmentRoutingModule {
}
