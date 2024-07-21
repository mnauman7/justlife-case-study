import { Injectable } from "@angular/core";
import { CanActivate, ActivatedRouteSnapshot, RouterStateSnapshot, Router } from "@angular/router";
import { Observable } from "rxjs";
import { AuthService } from "./auth.service";
import { AppRoutingModule } from "app/app-routing.module";

@Injectable()
export class AuthenticationGuard implements CanActivate {

  constructor(private auth: AuthService, private router: Router) { }

  canActivate(
    next: ActivatedRouteSnapshot,
    state: RouterStateSnapshot): Observable<boolean> | Promise<boolean> | boolean {

    if (!this.auth.isAuthenticated){
       console.log("not authenticated");
       return this.router.navigate(['login']);;
    } else {
        return true;
    }
  }
}