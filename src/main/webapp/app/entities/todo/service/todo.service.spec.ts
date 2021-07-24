import { TestBed } from '@angular/core/testing';
import { HttpClientTestingModule, HttpTestingController } from '@angular/common/http/testing';
import * as dayjs from 'dayjs';

import { DATE_TIME_FORMAT } from 'app/config/input.constants';
import { Priority } from 'app/entities/enumerations/priority.model';
import { ITodo, Todo } from '../todo.model';

import { TodoService } from './todo.service';

describe('Service Tests', () => {
  describe('Todo Service', () => {
    let service: TodoService;
    let httpMock: HttpTestingController;
    let elemDefault: ITodo;
    let expectedResult: ITodo | ITodo[] | boolean | null;
    let currentDate: dayjs.Dayjs;

    beforeEach(() => {
      TestBed.configureTestingModule({
        imports: [HttpClientTestingModule],
      });
      expectedResult = null;
      service = TestBed.inject(TodoService);
      httpMock = TestBed.inject(HttpTestingController);
      currentDate = dayjs();

      elemDefault = {
        id: 0,
        name: 'AAAAAAA',
        description: 'AAAAAAA',
        completed: false,
        progress: 0,
        order: 0,
        priority: Priority.HIGH,
        dueDate: currentDate,
      };
    });

    describe('Service methods', () => {
      it('should find an element', () => {
        const returnedFromService = Object.assign(
          {
            dueDate: currentDate.format(DATE_TIME_FORMAT),
          },
          elemDefault
        );

        service.find(123).subscribe(resp => (expectedResult = resp.body));

        const req = httpMock.expectOne({ method: 'GET' });
        req.flush(returnedFromService);
        expect(expectedResult).toMatchObject(elemDefault);
      });

      it('should create a Todo', () => {
        const returnedFromService = Object.assign(
          {
            id: 0,
            dueDate: currentDate.format(DATE_TIME_FORMAT),
          },
          elemDefault
        );

        const expected = Object.assign(
          {
            dueDate: currentDate,
          },
          returnedFromService
        );

        service.create(new Todo()).subscribe(resp => (expectedResult = resp.body));

        const req = httpMock.expectOne({ method: 'POST' });
        req.flush(returnedFromService);
        expect(expectedResult).toMatchObject(expected);
      });

      it('should update a Todo', () => {
        const returnedFromService = Object.assign(
          {
            id: 1,
            name: 'BBBBBB',
            description: 'BBBBBB',
            completed: true,
            progress: 1,
            order: 1,
            priority: 'BBBBBB',
            dueDate: currentDate.format(DATE_TIME_FORMAT),
          },
          elemDefault
        );

        const expected = Object.assign(
          {
            dueDate: currentDate,
          },
          returnedFromService
        );

        service.update(expected).subscribe(resp => (expectedResult = resp.body));

        const req = httpMock.expectOne({ method: 'PUT' });
        req.flush(returnedFromService);
        expect(expectedResult).toMatchObject(expected);
      });

      it('should partial update a Todo', () => {
        const patchObject = Object.assign(
          {
            description: 'BBBBBB',
            completed: true,
            progress: 1,
            priority: 'BBBBBB',
          },
          new Todo()
        );

        const returnedFromService = Object.assign(patchObject, elemDefault);

        const expected = Object.assign(
          {
            dueDate: currentDate,
          },
          returnedFromService
        );

        service.partialUpdate(patchObject).subscribe(resp => (expectedResult = resp.body));

        const req = httpMock.expectOne({ method: 'PATCH' });
        req.flush(returnedFromService);
        expect(expectedResult).toMatchObject(expected);
      });

      it('should return a list of Todo', () => {
        const returnedFromService = Object.assign(
          {
            id: 1,
            name: 'BBBBBB',
            description: 'BBBBBB',
            completed: true,
            progress: 1,
            order: 1,
            priority: 'BBBBBB',
            dueDate: currentDate.format(DATE_TIME_FORMAT),
          },
          elemDefault
        );

        const expected = Object.assign(
          {
            dueDate: currentDate,
          },
          returnedFromService
        );

        service.query().subscribe(resp => (expectedResult = resp.body));

        const req = httpMock.expectOne({ method: 'GET' });
        req.flush([returnedFromService]);
        httpMock.verify();
        expect(expectedResult).toContainEqual(expected);
      });

      it('should delete a Todo', () => {
        service.delete(123).subscribe(resp => (expectedResult = resp.ok));

        const req = httpMock.expectOne({ method: 'DELETE' });
        req.flush({ status: 200 });
        expect(expectedResult);
      });

      describe('addTodoToCollectionIfMissing', () => {
        it('should add a Todo to an empty array', () => {
          const todo: ITodo = { id: 123 };
          expectedResult = service.addTodoToCollectionIfMissing([], todo);
          expect(expectedResult).toHaveLength(1);
          expect(expectedResult).toContain(todo);
        });

        it('should not add a Todo to an array that contains it', () => {
          const todo: ITodo = { id: 123 };
          const todoCollection: ITodo[] = [
            {
              ...todo,
            },
            { id: 456 },
          ];
          expectedResult = service.addTodoToCollectionIfMissing(todoCollection, todo);
          expect(expectedResult).toHaveLength(2);
        });

        it("should add a Todo to an array that doesn't contain it", () => {
          const todo: ITodo = { id: 123 };
          const todoCollection: ITodo[] = [{ id: 456 }];
          expectedResult = service.addTodoToCollectionIfMissing(todoCollection, todo);
          expect(expectedResult).toHaveLength(2);
          expect(expectedResult).toContain(todo);
        });

        it('should add only unique Todo to an array', () => {
          const todoArray: ITodo[] = [{ id: 123 }, { id: 456 }, { id: 98861 }];
          const todoCollection: ITodo[] = [{ id: 123 }];
          expectedResult = service.addTodoToCollectionIfMissing(todoCollection, ...todoArray);
          expect(expectedResult).toHaveLength(3);
        });

        it('should accept varargs', () => {
          const todo: ITodo = { id: 123 };
          const todo2: ITodo = { id: 456 };
          expectedResult = service.addTodoToCollectionIfMissing([], todo, todo2);
          expect(expectedResult).toHaveLength(2);
          expect(expectedResult).toContain(todo);
          expect(expectedResult).toContain(todo2);
        });

        it('should accept null and undefined values', () => {
          const todo: ITodo = { id: 123 };
          expectedResult = service.addTodoToCollectionIfMissing([], null, todo, undefined);
          expect(expectedResult).toHaveLength(1);
          expect(expectedResult).toContain(todo);
        });

        it('should return initial array if no Todo is added', () => {
          const todoCollection: ITodo[] = [{ id: 123 }];
          expectedResult = service.addTodoToCollectionIfMissing(todoCollection, undefined, null);
          expect(expectedResult).toEqual(todoCollection);
        });
      });
    });

    afterEach(() => {
      httpMock.verify();
    });
  });
});
