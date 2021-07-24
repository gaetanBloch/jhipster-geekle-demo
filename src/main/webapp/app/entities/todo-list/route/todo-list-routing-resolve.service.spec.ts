jest.mock('@angular/router');

import { TestBed } from '@angular/core/testing';
import { HttpResponse } from '@angular/common/http';
import { HttpClientTestingModule } from '@angular/common/http/testing';
import { ActivatedRouteSnapshot, Router } from '@angular/router';
import { of } from 'rxjs';

import { ITodoList, TodoList } from '../todo-list.model';
import { TodoListService } from '../service/todo-list.service';

import { TodoListRoutingResolveService } from './todo-list-routing-resolve.service';

describe('Service Tests', () => {
  describe('TodoList routing resolve service', () => {
    let mockRouter: Router;
    let mockActivatedRouteSnapshot: ActivatedRouteSnapshot;
    let routingResolveService: TodoListRoutingResolveService;
    let service: TodoListService;
    let resultTodoList: ITodoList | undefined;

    beforeEach(() => {
      TestBed.configureTestingModule({
        imports: [HttpClientTestingModule],
        providers: [Router, ActivatedRouteSnapshot],
      });
      mockRouter = TestBed.inject(Router);
      mockActivatedRouteSnapshot = TestBed.inject(ActivatedRouteSnapshot);
      routingResolveService = TestBed.inject(TodoListRoutingResolveService);
      service = TestBed.inject(TodoListService);
      resultTodoList = undefined;
    });

    describe('resolve', () => {
      it('should return ITodoList returned by find', () => {
        // GIVEN
        service.find = jest.fn(id => of(new HttpResponse({ body: { id } })));
        mockActivatedRouteSnapshot.params = { id: 123 };

        // WHEN
        routingResolveService.resolve(mockActivatedRouteSnapshot).subscribe(result => {
          resultTodoList = result;
        });

        // THEN
        expect(service.find).toBeCalledWith(123);
        expect(resultTodoList).toEqual({ id: 123 });
      });

      it('should return new ITodoList if id is not provided', () => {
        // GIVEN
        service.find = jest.fn();
        mockActivatedRouteSnapshot.params = {};

        // WHEN
        routingResolveService.resolve(mockActivatedRouteSnapshot).subscribe(result => {
          resultTodoList = result;
        });

        // THEN
        expect(service.find).not.toBeCalled();
        expect(resultTodoList).toEqual(new TodoList());
      });

      it('should route to 404 page if data not found in server', () => {
        // GIVEN
        jest.spyOn(service, 'find').mockReturnValue(of(new HttpResponse({ body: null as unknown as TodoList })));
        mockActivatedRouteSnapshot.params = { id: 123 };

        // WHEN
        routingResolveService.resolve(mockActivatedRouteSnapshot).subscribe(result => {
          resultTodoList = result;
        });

        // THEN
        expect(service.find).toBeCalledWith(123);
        expect(resultTodoList).toEqual(undefined);
        expect(mockRouter.navigate).toHaveBeenCalledWith(['404']);
      });
    });
  });
});
