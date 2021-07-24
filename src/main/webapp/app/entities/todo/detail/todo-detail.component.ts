import { Component, OnInit } from '@angular/core';
import { ActivatedRoute } from '@angular/router';

import { ITodo } from '../todo.model';

@Component({
  selector: 'jhi-todo-detail',
  templateUrl: './todo-detail.component.html',
})
export class TodoDetailComponent implements OnInit {
  todo: ITodo | null = null;

  constructor(protected activatedRoute: ActivatedRoute) {}

  ngOnInit(): void {
    this.activatedRoute.data.subscribe(({ todo }) => {
      this.todo = todo;
    });
  }

  previousState(): void {
    window.history.back();
  }
}
