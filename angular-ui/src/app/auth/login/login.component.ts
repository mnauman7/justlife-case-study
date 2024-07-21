import {Component, OnInit} from '@angular/core';
import {Router} from '@angular/router';
import { AuthService } from '../auth.service';
import { CookieService } from 'ngx-cookie-service';
import { LoginCreds } from './login-creds';

@Component({
  selector: 'app-login',
  templateUrl: './login.component.html',
  styleUrls: ['./login.component.css']
})
export class LoginComponent implements OnInit {

  loginCreds: LoginCreds;
  errorMessage: string;

  constructor(private authService: AuthService, private cookieService: CookieService,
     private router: Router) {
    this.loginCreds = {} as LoginCreds;
  }

  ngOnInit() {
  }

  onSubmit(loginCreds: LoginCreds) {
    this.authService.loginUser(loginCreds).subscribe(
      response => {
        this.cookieService.set('token', response.token);
        this.authService.isAuthenticated = true;
        this.goToHomePage();
      },
      error => this.errorMessage = error as any
    );
  }

  goToHomePage() {
    this.router.navigate(['welcome']);
  }

}
