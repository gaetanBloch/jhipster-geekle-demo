import { NgModule } from '@angular/core';
import { RouterModule, Routes } from '@angular/router';

import { UserRouteAccessService } from 'app/core/auth/user-route-access.service';
import { TodoComponent } from '../list/todo.component';
import { TodoDetailComponent } from '../detail/todo-detail.component';
import { TodoUpdateComponent } from '../update/todo-update.component';
import { TodoRoutingResolveService } from './todo-routing-resolve.service';

const todoRoute: Routes = [
  {
    path: '',
    component: TodoComponent,
    canActivate: [UserRouteAccessService],
  },
  {
    path: ':id/view',
    component: TodoDetailComponent,
    resolve: {
      todo: TodoRoutingResolveService,
    },
    canActivate: [UserRouteAccessService],
  },
  {
    path: 'new',
    component: TodoUpdateComponent,
    resolve: {
      todo: TodoRoutingResolveService,
    },
    canActivate: [UserRouteAccessService],
  },
  {
    path: ':id/edit',
    component: TodoUpdateComponent,
    resolve: {
      todo: TodoRoutingResolveService,
    },
    canActivate: [UserRouteAccessService],
  },
];

@NgModule({
  imports: [RouterModule.forChild(todoRoute)],
  exports: [RouterModule],
})
export class TodoRoutingModule {}
