import { Component, OnInit } from '@angular/core';
import { ActivatedRoute } from '@angular/router';

import { ITodoList } from '../todo-list.model';

@Component({
  selector: 'jhi-todo-list-detail',
  templateUrl: './todo-list-detail.component.html',
})
export class TodoListDetailComponent implements OnInit {
  todoList: ITodoList | null = null;

  constructor(protected activatedRoute: ActivatedRoute) {}

  ngOnInit(): void {
    this.activatedRoute.data.subscribe(({ todoList }) => {
      this.todoList = todoList;
    });
  }

  previousState(): void {
    window.history.back();
  }
}
