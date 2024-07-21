import {NgModule} from '@angular/core';
import {RouterModule, Routes} from '@angular/router';
import {UserDetailComponent} from './user-detail/user-detail.component';
import {UserListComponent} from './user-list/user-list.component';
import {UserEditComponent} from './user-edit/user-edit.component';
import {UserAddComponent} from './user-add/user-add.component';

const userRoutes: Routes = [
  {path: 'users', component: UserListComponent},
  {path: 'users/add', component: UserAddComponent},
  {path: 'users/:id', component: UserDetailComponent},
  {path: 'users/:id/edit', component: UserEditComponent}
];

@NgModule({
  imports: [RouterModule.forChild(userRoutes)],
  exports: [RouterModule]
})

export class UsersRoutingModule {
}
