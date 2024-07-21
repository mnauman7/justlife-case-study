import { Component, OnInit } from '@angular/core';
import { UserService } from '../user.service';
import { User } from '../user';
import { ActivatedRoute, Router } from '@angular/router';

@Component({
  selector: 'app-user-edit',
  templateUrl: './user-edit.component.html',
  styleUrls: ['./user-edit.component.css'],
})
export class UserEditComponent implements OnInit {
  user: User;
  errorMessage: string; // server error message
  userId: number;
  constructor(
    private userService: UserService,
    private route: ActivatedRoute,
    private router: Router
  ) {
    this.user = {} as User;
  }

  ngOnInit() {
    const userId = this.route.snapshot.params.id;
    this.userService.getUserDetails(userId).subscribe(
      (user) => (this.user = user),
      (error) => (this.errorMessage = error as any)
    );
  }

  onSubmit(user: User) {
    const that = this;  
    const userId = this.route.snapshot.params.id;
    this.userService.updateUser(userId , user).subscribe(
      (res) => this.gotoUserDetail(user),
      (error) => (this.errorMessage = error as any)
    );
  }

  gotoUserDetail(user: User) {
    this.errorMessage = null;
    this.router.navigate(['/users', user.id]);
  }
}
