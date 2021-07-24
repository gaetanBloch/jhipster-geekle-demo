import { NgModule } from '@angular/core';
import { SharedModule } from 'app/shared/shared.module';
import { TodoListComponent } from './list/todo-list.component';
import { TodoListDetailComponent } from './detail/todo-list-detail.component';
import { TodoListUpdateComponent } from './update/todo-list-update.component';
import { TodoListDeleteDialogComponent } from './delete/todo-list-delete-dialog.component';
import { TodoListRoutingModule } from './route/todo-list-routing.module';

@NgModule({
  imports: [SharedModule, TodoListRoutingModule],
  declarations: [TodoListComponent, TodoListDetailComponent, TodoListUpdateComponent, TodoListDeleteDialogComponent],
  entryComponents: [TodoListDeleteDialogComponent],
})
export class TodoListModule {}
