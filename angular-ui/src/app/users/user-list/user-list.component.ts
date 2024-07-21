import {Component, OnInit} from '@angular/core';
import {UserService} from '../user.service';
import {User} from '../user';
import {Router} from '@angular/router';
import { finalize } from 'rxjs/operators';

@Component({
  selector: 'app-user-list',
  templateUrl: './user-list.component.html',
  styleUrls: ['./user-list.component.css']
})
export class UserListComponent implements OnInit {
  errorMessage: string;
  searchValue: string;
  users: User[];
  listOfUsersWithLastName: User[];
  isUsersDataReceived: boolean = false;

  constructor(private router: Router, private userService: UserService) {

  }

  ngOnInit() {
    this.userService.getUsers().pipe(
      finalize(() => {
        this.isUsersDataReceived = true;
      })
    ).subscribe(
      users => this.users = users,
      error => this.errorMessage = error as any);
  }

  onSelect(user: User) {
    this.router.navigate(['/users', user.id]);
  }

  addUser() {
    this.router.navigate(['/users/add']);
  }

  searchUsers(searchValue: string)
  {
      if (searchValue === '')
      {
      this.userService.getUsers()
      .subscribe(
            (users) => {
             this.users = users;
            });
      }

      if (searchValue !== '')
      {
      this.userService.searchUsers(searchValue)
      .subscribe(
      (users) => {

       this.users = users;

       },
       (error) =>
       {
         this.users = null;
       }
      );

      }
  }


}
