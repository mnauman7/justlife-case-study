import {NgModule} from '@angular/core';
import {UserService} from './user.service';
import {UserListComponent} from './user-list/user-list.component';
import {UserDetailComponent} from './user-detail/user-detail.component';
import {CommonModule} from '@angular/common';
import {FormsModule} from '@angular/forms';
import {UserAddComponent} from './user-add/user-add.component';
import {UserEditComponent} from './user-edit/user-edit.component';
import {UsersRoutingModule} from './user-routing.module';

@NgModule({
  imports: [
    CommonModule,
    FormsModule,
    UsersRoutingModule
  ],
  declarations: [
    UserListComponent,
    UserDetailComponent,
    UserEditComponent,
    UserAddComponent
  ],
  providers: [UserService]

})

export class UsersModule {
}
