import { Component, OnInit } from '@angular/core';
import { HttpResponse } from '@angular/common/http';
import { NgbModal } from '@ng-bootstrap/ng-bootstrap';

import { ITodoList } from '../todo-list.model';
import { TodoListService } from '../service/todo-list.service';
import { TodoListDeleteDialogComponent } from '../delete/todo-list-delete-dialog.component';

@Component({
  selector: 'jhi-todo-list',
  templateUrl: './todo-list.component.html',
})
export class TodoListComponent implements OnInit {
  todoLists?: ITodoList[];
  isLoading = false;

  constructor(protected todoListService: TodoListService, protected modalService: NgbModal) {}

  loadAll(): void {
    this.isLoading = true;

    this.todoListService.query().subscribe(
      (res: HttpResponse<ITodoList[]>) => {
        this.isLoading = false;
        this.todoLists = res.body ?? [];
      },
      () => {
        this.isLoading = false;
      }
    );
  }

  ngOnInit(): void {
    this.loadAll();
  }

  trackId(index: number, item: ITodoList): number {
    return item.id!;
  }

  delete(todoList: ITodoList): void {
    const modalRef = this.modalService.open(TodoListDeleteDialogComponent, { size: 'lg', backdrop: 'static' });
    modalRef.componentInstance.todoList = todoList;
    // unsubscribe not needed because closed completes on modal close
    modalRef.closed.subscribe(reason => {
      if (reason === 'deleted') {
        this.loadAll();
      }
    });
  }
}
