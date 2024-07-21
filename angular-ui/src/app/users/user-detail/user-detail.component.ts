import {Component, OnInit} from '@angular/core';
import {UserService} from '../user.service';
import {ActivatedRoute, Router} from '@angular/router';
import {User} from '../user';


@Component({
  selector: 'app-user-detail',
  templateUrl: './user-detail.component.html',
  styleUrls: ['./user-detail.component.css']
})
export class UserDetailComponent implements OnInit {
  errorMessage: string;
  user: User;

  constructor(private route: ActivatedRoute, private router: Router, private userService: UserService) {
    this.user = {} as User;
  }

  ngOnInit() {
    const userId = this.route.snapshot.params.id;
    this.userService.getUserById(userId).subscribe(
      user => this.user = user,
      error => this.errorMessage = error as any);
  }

  gotoUsersList() {
    this.router.navigate(['/users']);
  }

  editUser() {
    this.router.navigate(['/users', this.user.id, 'edit']);
  }

  deactivateUser() {
    const that = this;  
    const userId = this.route.snapshot.params.id;
    this.userService.updateUserActiveStatus(userId , false).subscribe(
      (res) => this.gotoUsersList(),
      (error) => (this.errorMessage = error as any)
    );
  }

}
