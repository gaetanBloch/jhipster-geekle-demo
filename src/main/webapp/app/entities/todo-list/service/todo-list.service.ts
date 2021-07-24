import { Injectable } from '@angular/core';
import { HttpClient, HttpResponse } from '@angular/common/http';
import { Observable } from 'rxjs';

import { isPresent } from 'app/core/util/operators';
import { ApplicationConfigService } from 'app/core/config/application-config.service';
import { createRequestOption } from 'app/core/request/request-util';
import { ITodoList, getTodoListIdentifier } from '../todo-list.model';

export type EntityResponseType = HttpResponse<ITodoList>;
export type EntityArrayResponseType = HttpResponse<ITodoList[]>;

@Injectable({ providedIn: 'root' })
export class TodoListService {
  protected resourceUrl = this.applicationConfigService.getEndpointFor('api/todo-lists');

  constructor(protected http: HttpClient, protected applicationConfigService: ApplicationConfigService) {}

  create(todoList: ITodoList): Observable<EntityResponseType> {
    return this.http.post<ITodoList>(this.resourceUrl, todoList, { observe: 'response' });
  }

  update(todoList: ITodoList): Observable<EntityResponseType> {
    return this.http.put<ITodoList>(`${this.resourceUrl}/${getTodoListIdentifier(todoList) as number}`, todoList, { observe: 'response' });
  }

  partialUpdate(todoList: ITodoList): Observable<EntityResponseType> {
    return this.http.patch<ITodoList>(`${this.resourceUrl}/${getTodoListIdentifier(todoList) as number}`, todoList, {
      observe: 'response',
    });
  }

  find(id: number): Observable<EntityResponseType> {
    return this.http.get<ITodoList>(`${this.resourceUrl}/${id}`, { observe: 'response' });
  }

  query(req?: any): Observable<EntityArrayResponseType> {
    const options = createRequestOption(req);
    return this.http.get<ITodoList[]>(this.resourceUrl, { params: options, observe: 'response' });
  }

  delete(id: number): Observable<HttpResponse<{}>> {
    return this.http.delete(`${this.resourceUrl}/${id}`, { observe: 'response' });
  }

  addTodoListToCollectionIfMissing(todoListCollection: ITodoList[], ...todoListsToCheck: (ITodoList | null | undefined)[]): ITodoList[] {
    const todoLists: ITodoList[] = todoListsToCheck.filter(isPresent);
    if (todoLists.length > 0) {
      const todoListCollectionIdentifiers = todoListCollection.map(todoListItem => getTodoListIdentifier(todoListItem)!);
      const todoListsToAdd = todoLists.filter(todoListItem => {
        const todoListIdentifier = getTodoListIdentifier(todoListItem);
        if (todoListIdentifier == null || todoListCollectionIdentifiers.includes(todoListIdentifier)) {
          return false;
        }
        todoListCollectionIdentifiers.push(todoListIdentifier);
        return true;
      });
      return [...todoListsToAdd, ...todoListCollection];
    }
    return todoListCollection;
  }
}
