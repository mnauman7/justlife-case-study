import {Component} from '@angular/core';
import { LoginComponent } from './auth/login/login.component';

@Component({
  selector: 'app-root',
  templateUrl: './app.component.html',
  styleUrls: ['./app.component.css']
})
export class AppComponent {
  showHeaders = true;

  refreshHeaders(event){
    //hide headers on login page
    this.showHeaders = !(event instanceof LoginComponent);                                                   
  } 
}
