import { NgModule } from '@angular/core';
import { RouterModule, Routes } from '@angular/router';

import { UserRouteAccessService } from 'app/core/auth/user-route-access.service';
import { TodoListComponent } from '../list/todo-list.component';
import { TodoListDetailComponent } from '../detail/todo-list-detail.component';
import { TodoListUpdateComponent } from '../update/todo-list-update.component';
import { TodoListRoutingResolveService } from './todo-list-routing-resolve.service';

const todoListRoute: Routes = [
  {
    path: '',
    component: TodoListComponent,
    canActivate: [UserRouteAccessService],
  },
  {
    path: ':id/view',
    component: TodoListDetailComponent,
    resolve: {
      todoList: TodoListRoutingResolveService,
    },
    canActivate: [UserRouteAccessService],
  },
  {
    path: 'new',
    component: TodoListUpdateComponent,
    resolve: {
      todoList: TodoListRoutingResolveService,
    },
    canActivate: [UserRouteAccessService],
  },
  {
    path: ':id/edit',
    component: TodoListUpdateComponent,
    resolve: {
      todoList: TodoListRoutingResolveService,
    },
    canActivate: [UserRouteAccessService],
  },
];

@NgModule({
  imports: [RouterModule.forChild(todoListRoute)],
  exports: [RouterModule],
})
export class TodoListRoutingModule {}
