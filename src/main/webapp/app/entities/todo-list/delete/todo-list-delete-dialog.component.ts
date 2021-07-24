import { Component } from '@angular/core';
import { NgbActiveModal } from '@ng-bootstrap/ng-bootstrap';

import { ITodoList } from '../todo-list.model';
import { TodoListService } from '../service/todo-list.service';

@Component({
  templateUrl: './todo-list-delete-dialog.component.html',
})
export class TodoListDeleteDialogComponent {
  todoList?: ITodoList;

  constructor(protected todoListService: TodoListService, protected activeModal: NgbActiveModal) {}

  cancel(): void {
    this.activeModal.dismiss();
  }

  confirmDelete(id: number): void {
    this.todoListService.delete(id).subscribe(() => {
      this.activeModal.close('deleted');
    });
  }
}
