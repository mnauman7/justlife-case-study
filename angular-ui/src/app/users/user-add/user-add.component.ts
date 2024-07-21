import {Component, OnInit} from '@angular/core';
import {UserService} from '../user.service';
import {User} from '../user';
import {Router} from '@angular/router';

@Component({
  selector: 'app-user-add',
  templateUrl: './user-add.component.html',
  styleUrls: ['./user-add.component.css']
})
export class UserAddComponent implements OnInit {

  user: User;
  errorMessage: string;

  constructor(private userService: UserService, private router: Router) {
    this.user = {} as User;
  }

  ngOnInit() {
  }

  onSubmit(user: User) {
    user.id = null;
    this.userService.addUser(user).subscribe(
      newUser => {
        this.user = newUser;
        this.gotoUsersList();
      },
      error => this.errorMessage = error as any
    );
  }

  gotoUsersList() {
    this.router.navigate(['/users']);
  }

}
