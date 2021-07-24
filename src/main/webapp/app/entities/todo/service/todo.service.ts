import { Injectable } from '@angular/core';
import { HttpClient, HttpResponse } from '@angular/common/http';
import { Observable } from 'rxjs';
import { map } from 'rxjs/operators';
import * as dayjs from 'dayjs';

import { isPresent } from 'app/core/util/operators';
import { ApplicationConfigService } from 'app/core/config/application-config.service';
import { createRequestOption } from 'app/core/request/request-util';
import { ITodo, getTodoIdentifier } from '../todo.model';

export type EntityResponseType = HttpResponse<ITodo>;
export type EntityArrayResponseType = HttpResponse<ITodo[]>;

@Injectable({ providedIn: 'root' })
export class TodoService {
  protected resourceUrl = this.applicationConfigService.getEndpointFor('api/todos');

  constructor(protected http: HttpClient, protected applicationConfigService: ApplicationConfigService) {}

  create(todo: ITodo): Observable<EntityResponseType> {
    const copy = this.convertDateFromClient(todo);
    return this.http
      .post<ITodo>(this.resourceUrl, copy, { observe: 'response' })
      .pipe(map((res: EntityResponseType) => this.convertDateFromServer(res)));
  }

  update(todo: ITodo): Observable<EntityResponseType> {
    const copy = this.convertDateFromClient(todo);
    return this.http
      .put<ITodo>(`${this.resourceUrl}/${getTodoIdentifier(todo) as number}`, copy, { observe: 'response' })
      .pipe(map((res: EntityResponseType) => this.convertDateFromServer(res)));
  }

  partialUpdate(todo: ITodo): Observable<EntityResponseType> {
    const copy = this.convertDateFromClient(todo);
    return this.http
      .patch<ITodo>(`${this.resourceUrl}/${getTodoIdentifier(todo) as number}`, copy, { observe: 'response' })
      .pipe(map((res: EntityResponseType) => this.convertDateFromServer(res)));
  }

  find(id: number): Observable<EntityResponseType> {
    return this.http
      .get<ITodo>(`${this.resourceUrl}/${id}`, { observe: 'response' })
      .pipe(map((res: EntityResponseType) => this.convertDateFromServer(res)));
  }

  query(req?: any): Observable<EntityArrayResponseType> {
    const options = createRequestOption(req);
    return this.http
      .get<ITodo[]>(this.resourceUrl, { params: options, observe: 'response' })
      .pipe(map((res: EntityArrayResponseType) => this.convertDateArrayFromServer(res)));
  }

  delete(id: number): Observable<HttpResponse<{}>> {
    return this.http.delete(`${this.resourceUrl}/${id}`, { observe: 'response' });
  }

  addTodoToCollectionIfMissing(todoCollection: ITodo[], ...todosToCheck: (ITodo | null | undefined)[]): ITodo[] {
    const todos: ITodo[] = todosToCheck.filter(isPresent);
    if (todos.length > 0) {
      const todoCollectionIdentifiers = todoCollection.map(todoItem => getTodoIdentifier(todoItem)!);
      const todosToAdd = todos.filter(todoItem => {
        const todoIdentifier = getTodoIdentifier(todoItem);
        if (todoIdentifier == null || todoCollectionIdentifiers.includes(todoIdentifier)) {
          return false;
        }
        todoCollectionIdentifiers.push(todoIdentifier);
        return true;
      });
      return [...todosToAdd, ...todoCollection];
    }
    return todoCollection;
  }

  protected convertDateFromClient(todo: ITodo): ITodo {
    return Object.assign({}, todo, {
      dueDate: todo.dueDate?.isValid() ? todo.dueDate.toJSON() : undefined,
    });
  }

  protected convertDateFromServer(res: EntityResponseType): EntityResponseType {
    if (res.body) {
      res.body.dueDate = res.body.dueDate ? dayjs(res.body.dueDate) : undefined;
    }
    return res;
  }

  protected convertDateArrayFromServer(res: EntityArrayResponseType): EntityArrayResponseType {
    if (res.body) {
      res.body.forEach((todo: ITodo) => {
        todo.dueDate = todo.dueDate ? dayjs(todo.dueDate) : undefined;
      });
    }
    return res;
  }
}
