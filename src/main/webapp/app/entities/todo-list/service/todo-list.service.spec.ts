import { TestBed } from '@angular/core/testing';
import { HttpClientTestingModule, HttpTestingController } from '@angular/common/http/testing';

import { ITodoList, TodoList } from '../todo-list.model';

import { TodoListService } from './todo-list.service';

describe('Service Tests', () => {
  describe('TodoList Service', () => {
    let service: TodoListService;
    let httpMock: HttpTestingController;
    let elemDefault: ITodoList;
    let expectedResult: ITodoList | ITodoList[] | boolean | null;

    beforeEach(() => {
      TestBed.configureTestingModule({
        imports: [HttpClientTestingModule],
      });
      expectedResult = null;
      service = TestBed.inject(TodoListService);
      httpMock = TestBed.inject(HttpTestingController);

      elemDefault = {
        id: 0,
        title: 'AAAAAAA',
      };
    });

    describe('Service methods', () => {
      it('should find an element', () => {
        const returnedFromService = Object.assign({}, elemDefault);

        service.find(123).subscribe(resp => (expectedResult = resp.body));

        const req = httpMock.expectOne({ method: 'GET' });
        req.flush(returnedFromService);
        expect(expectedResult).toMatchObject(elemDefault);
      });

      it('should create a TodoList', () => {
        const returnedFromService = Object.assign(
          {
            id: 0,
          },
          elemDefault
        );

        const expected = Object.assign({}, returnedFromService);

        service.create(new TodoList()).subscribe(resp => (expectedResult = resp.body));

        const req = httpMock.expectOne({ method: 'POST' });
        req.flush(returnedFromService);
        expect(expectedResult).toMatchObject(expected);
      });

      it('should update a TodoList', () => {
        const returnedFromService = Object.assign(
          {
            id: 1,
            title: 'BBBBBB',
          },
          elemDefault
        );

        const expected = Object.assign({}, returnedFromService);

        service.update(expected).subscribe(resp => (expectedResult = resp.body));

        const req = httpMock.expectOne({ method: 'PUT' });
        req.flush(returnedFromService);
        expect(expectedResult).toMatchObject(expected);
      });

      it('should partial update a TodoList', () => {
        const patchObject = Object.assign(
          {
            title: 'BBBBBB',
          },
          new TodoList()
        );

        const returnedFromService = Object.assign(patchObject, elemDefault);

        const expected = Object.assign({}, returnedFromService);

        service.partialUpdate(patchObject).subscribe(resp => (expectedResult = resp.body));

        const req = httpMock.expectOne({ method: 'PATCH' });
        req.flush(returnedFromService);
        expect(expectedResult).toMatchObject(expected);
      });

      it('should return a list of TodoList', () => {
        const returnedFromService = Object.assign(
          {
            id: 1,
            title: 'BBBBBB',
          },
          elemDefault
        );

        const expected = Object.assign({}, returnedFromService);

        service.query().subscribe(resp => (expectedResult = resp.body));

        const req = httpMock.expectOne({ method: 'GET' });
        req.flush([returnedFromService]);
        httpMock.verify();
        expect(expectedResult).toContainEqual(expected);
      });

      it('should delete a TodoList', () => {
        service.delete(123).subscribe(resp => (expectedResult = resp.ok));

        const req = httpMock.expectOne({ method: 'DELETE' });
        req.flush({ status: 200 });
        expect(expectedResult);
      });

      describe('addTodoListToCollectionIfMissing', () => {
        it('should add a TodoList to an empty array', () => {
          const todoList: ITodoList = { id: 123 };
          expectedResult = service.addTodoListToCollectionIfMissing([], todoList);
          expect(expectedResult).toHaveLength(1);
          expect(expectedResult).toContain(todoList);
        });

        it('should not add a TodoList to an array that contains it', () => {
          const todoList: ITodoList = { id: 123 };
          const todoListCollection: ITodoList[] = [
            {
              ...todoList,
            },
            { id: 456 },
          ];
          expectedResult = service.addTodoListToCollectionIfMissing(todoListCollection, todoList);
          expect(expectedResult).toHaveLength(2);
        });

        it("should add a TodoList to an array that doesn't contain it", () => {
          const todoList: ITodoList = { id: 123 };
          const todoListCollection: ITodoList[] = [{ id: 456 }];
          expectedResult = service.addTodoListToCollectionIfMissing(todoListCollection, todoList);
          expect(expectedResult).toHaveLength(2);
          expect(expectedResult).toContain(todoList);
        });

        it('should add only unique TodoList to an array', () => {
          const todoListArray: ITodoList[] = [{ id: 123 }, { id: 456 }, { id: 44527 }];
          const todoListCollection: ITodoList[] = [{ id: 123 }];
          expectedResult = service.addTodoListToCollectionIfMissing(todoListCollection, ...todoListArray);
          expect(expectedResult).toHaveLength(3);
        });

        it('should accept varargs', () => {
          const todoList: ITodoList = { id: 123 };
          const todoList2: ITodoList = { id: 456 };
          expectedResult = service.addTodoListToCollectionIfMissing([], todoList, todoList2);
          expect(expectedResult).toHaveLength(2);
          expect(expectedResult).toContain(todoList);
          expect(expectedResult).toContain(todoList2);
        });

        it('should accept null and undefined values', () => {
          const todoList: ITodoList = { id: 123 };
          expectedResult = service.addTodoListToCollectionIfMissing([], null, todoList, undefined);
          expect(expectedResult).toHaveLength(1);
          expect(expectedResult).toContain(todoList);
        });

        it('should return initial array if no TodoList is added', () => {
          const todoListCollection: ITodoList[] = [{ id: 123 }];
          expectedResult = service.addTodoListToCollectionIfMissing(todoListCollection, undefined, null);
          expect(expectedResult).toEqual(todoListCollection);
        });
      });
    });

    afterEach(() => {
      httpMock.verify();
    });
  });
});
