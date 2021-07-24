import { NgModule } from '@angular/core';
import { SharedModule } from 'app/shared/shared.module';
import { TodoComponent } from './list/todo.component';
import { TodoDetailComponent } from './detail/todo-detail.component';
import { TodoUpdateComponent } from './update/todo-update.component';
import { TodoDeleteDialogComponent } from './delete/todo-delete-dialog.component';
import { TodoRoutingModule } from './route/todo-routing.module';

@NgModule({
  imports: [SharedModule, TodoRoutingModule],
  declarations: [TodoComponent, TodoDetailComponent, TodoUpdateComponent, TodoDeleteDialogComponent],
  entryComponents: [TodoDeleteDialogComponent],
})
export class TodoModule {}
