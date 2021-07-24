import { Component } from '@angular/core';
import { NgbActiveModal } from '@ng-bootstrap/ng-bootstrap';

import { ITodo } from '../todo.model';
import { TodoService } from '../service/todo.service';

@Component({
  templateUrl: './todo-delete-dialog.component.html',
})
export class TodoDeleteDialogComponent {
  todo?: ITodo;

  constructor(protected todoService: TodoService, protected activeModal: NgbActiveModal) {}

  cancel(): void {
    this.activeModal.dismiss();
  }

  confirmDelete(id: number): void {
    this.todoService.delete(id).subscribe(() => {
      this.activeModal.close('deleted');
    });
  }
}
