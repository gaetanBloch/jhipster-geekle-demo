import { Injectable } from '@angular/core';
import { HttpResponse } from '@angular/common/http';
import { Resolve, ActivatedRouteSnapshot, Router } from '@angular/router';
import { Observable, of, EMPTY } from 'rxjs';
import { mergeMap } from 'rxjs/operators';

import { ITodo, Todo } from '../todo.model';
import { TodoService } from '../service/todo.service';

@Injectable({ providedIn: 'root' })
export class TodoRoutingResolveService implements Resolve<ITodo> {
  constructor(protected service: TodoService, protected router: Router) {}

  resolve(route: ActivatedRouteSnapshot): Observable<ITodo> | Observable<never> {
    const id = route.params['id'];
    if (id) {
      return this.service.find(id).pipe(
        mergeMap((todo: HttpResponse<Todo>) => {
          if (todo.body) {
            return of(todo.body);
          } else {
            this.router.navigate(['404']);
            return EMPTY;
          }
        })
      );
    }
    return of(new Todo());
  }
}
