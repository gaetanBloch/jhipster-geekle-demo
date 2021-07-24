import { Component, OnInit } from '@angular/core';
import { HttpResponse } from '@angular/common/http';
import { FormBuilder, Validators } from '@angular/forms';
import { ActivatedRoute } from '@angular/router';
import { Observable } from 'rxjs';
import { finalize, map } from 'rxjs/operators';

import * as dayjs from 'dayjs';
import { DATE_TIME_FORMAT } from 'app/config/input.constants';

import { ITodo, Todo } from '../todo.model';
import { TodoService } from '../service/todo.service';
import { ICategory } from 'app/entities/category/category.model';
import { CategoryService } from 'app/entities/category/service/category.service';
import { ITodoList } from 'app/entities/todo-list/todo-list.model';
import { TodoListService } from 'app/entities/todo-list/service/todo-list.service';
import { ITag } from 'app/entities/tag/tag.model';
import { TagService } from 'app/entities/tag/service/tag.service';

@Component({
  selector: 'jhi-todo-update',
  templateUrl: './todo-update.component.html',
})
export class TodoUpdateComponent implements OnInit {
  isSaving = false;

  categoriesSharedCollection: ICategory[] = [];
  todoListsSharedCollection: ITodoList[] = [];
  tagsSharedCollection: ITag[] = [];

  editForm = this.fb.group({
    id: [],
    name: [null, [Validators.required, Validators.minLength(3)]],
    description: [null, [Validators.minLength(3)]],
    completed: [null, [Validators.required]],
    progress: [null, [Validators.min(0)]],
    order: [null, [Validators.min(0)]],
    priority: [],
    dueDate: [],
    category: [],
    todoList: [],
    tags: [],
  });

  constructor(
    protected todoService: TodoService,
    protected categoryService: CategoryService,
    protected todoListService: TodoListService,
    protected tagService: TagService,
    protected activatedRoute: ActivatedRoute,
    protected fb: FormBuilder
  ) {}

  ngOnInit(): void {
    this.activatedRoute.data.subscribe(({ todo }) => {
      if (todo.id === undefined) {
        const today = dayjs().startOf('day');
        todo.dueDate = today;
      }

      this.updateForm(todo);

      this.loadRelationshipsOptions();
    });
  }

  previousState(): void {
    window.history.back();
  }

  save(): void {
    this.isSaving = true;
    const todo = this.createFromForm();
    if (todo.id !== undefined) {
      this.subscribeToSaveResponse(this.todoService.update(todo));
    } else {
      this.subscribeToSaveResponse(this.todoService.create(todo));
    }
  }

  trackCategoryById(index: number, item: ICategory): number {
    return item.id!;
  }

  trackTodoListById(index: number, item: ITodoList): number {
    return item.id!;
  }

  trackTagById(index: number, item: ITag): number {
    return item.id!;
  }

  getSelectedTag(option: ITag, selectedVals?: ITag[]): ITag {
    if (selectedVals) {
      for (const selectedVal of selectedVals) {
        if (option.id === selectedVal.id) {
          return selectedVal;
        }
      }
    }
    return option;
  }

  protected subscribeToSaveResponse(result: Observable<HttpResponse<ITodo>>): void {
    result.pipe(finalize(() => this.onSaveFinalize())).subscribe(
      () => this.onSaveSuccess(),
      () => this.onSaveError()
    );
  }

  protected onSaveSuccess(): void {
    this.previousState();
  }

  protected onSaveError(): void {
    // Api for inheritance.
  }

  protected onSaveFinalize(): void {
    this.isSaving = false;
  }

  protected updateForm(todo: ITodo): void {
    this.editForm.patchValue({
      id: todo.id,
      name: todo.name,
      description: todo.description,
      completed: todo.completed,
      progress: todo.progress,
      order: todo.order,
      priority: todo.priority,
      dueDate: todo.dueDate ? todo.dueDate.format(DATE_TIME_FORMAT) : null,
      category: todo.category,
      todoList: todo.todoList,
      tags: todo.tags,
    });

    this.categoriesSharedCollection = this.categoryService.addCategoryToCollectionIfMissing(this.categoriesSharedCollection, todo.category);
    this.todoListsSharedCollection = this.todoListService.addTodoListToCollectionIfMissing(this.todoListsSharedCollection, todo.todoList);
    this.tagsSharedCollection = this.tagService.addTagToCollectionIfMissing(this.tagsSharedCollection, ...(todo.tags ?? []));
  }

  protected loadRelationshipsOptions(): void {
    this.categoryService
      .query()
      .pipe(map((res: HttpResponse<ICategory[]>) => res.body ?? []))
      .pipe(
        map((categories: ICategory[]) =>
          this.categoryService.addCategoryToCollectionIfMissing(categories, this.editForm.get('category')!.value)
        )
      )
      .subscribe((categories: ICategory[]) => (this.categoriesSharedCollection = categories));

    this.todoListService
      .query()
      .pipe(map((res: HttpResponse<ITodoList[]>) => res.body ?? []))
      .pipe(
        map((todoLists: ITodoList[]) =>
          this.todoListService.addTodoListToCollectionIfMissing(todoLists, this.editForm.get('todoList')!.value)
        )
      )
      .subscribe((todoLists: ITodoList[]) => (this.todoListsSharedCollection = todoLists));

    this.tagService
      .query()
      .pipe(map((res: HttpResponse<ITag[]>) => res.body ?? []))
      .pipe(map((tags: ITag[]) => this.tagService.addTagToCollectionIfMissing(tags, ...(this.editForm.get('tags')!.value ?? []))))
      .subscribe((tags: ITag[]) => (this.tagsSharedCollection = tags));
  }

  protected createFromForm(): ITodo {
    return {
      ...new Todo(),
      id: this.editForm.get(['id'])!.value,
      name: this.editForm.get(['name'])!.value,
      description: this.editForm.get(['description'])!.value,
      completed: this.editForm.get(['completed'])!.value,
      progress: this.editForm.get(['progress'])!.value,
      order: this.editForm.get(['order'])!.value,
      priority: this.editForm.get(['priority'])!.value,
      dueDate: this.editForm.get(['dueDate'])!.value ? dayjs(this.editForm.get(['dueDate'])!.value, DATE_TIME_FORMAT) : undefined,
      category: this.editForm.get(['category'])!.value,
      todoList: this.editForm.get(['todoList'])!.value,
      tags: this.editForm.get(['tags'])!.value,
    };
  }
}
