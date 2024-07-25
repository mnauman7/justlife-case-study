import {BrowserModule} from '@angular/platform-browser';
import {NgModule} from '@angular/core';
import {FormsModule} from '@angular/forms';
import {HTTP_INTERCEPTORS, HttpClientModule} from '@angular/common/http';
import {AppComponent} from './app.component';
import {AppRoutingModule} from './app-routing.module';
import {PartsModule} from './parts/parts.module';
import {HttpErrorHandler} from './error.service';
import {BrowserAnimationsModule} from '@angular/platform-browser/animations';
import { UsersModule } from './users/user.module';
import { AuthModule } from './auth/auth.module';
import { AuthenticationGuard } from './auth/auth-guard';
import { HeaderComponent } from './app-header/app-header.component';
import { JwtInterceptor } from './auth/jwt-intercepter';
import { BookingModule } from './booking/booking.module';
import { AppointmentsModule } from './appointments/appointment.module';


@NgModule({
  declarations: [
    AppComponent,
    HeaderComponent
  ],
  imports: [
    BrowserModule,
    FormsModule,
    HttpClientModule,
    UsersModule,
    BookingModule,
    AppointmentsModule,
    AuthModule,
    PartsModule,
    BrowserAnimationsModule,
    AppRoutingModule
  ],
  providers: [
    HttpErrorHandler,
    AuthenticationGuard,
    {
      //registering jwt interceptor for all requests
      provide: HTTP_INTERCEPTORS,
      useClass: JwtInterceptor,
      multi: true
    }
  ],
  bootstrap: [AppComponent]
})
export class AppModule {
}
