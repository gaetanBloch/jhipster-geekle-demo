jest.mock('@angular/router');

import { ComponentFixture, TestBed } from '@angular/core/testing';
import { HttpResponse } from '@angular/common/http';
import { HttpClientTestingModule } from '@angular/common/http/testing';
import { FormBuilder } from '@angular/forms';
import { ActivatedRoute } from '@angular/router';
import { of, Subject } from 'rxjs';

import { TodoListService } from '../service/todo-list.service';
import { ITodoList, TodoList } from '../todo-list.model';

import { IUser } from 'app/entities/user/user.model';
import { UserService } from 'app/entities/user/user.service';

import { TodoListUpdateComponent } from './todo-list-update.component';

describe('Component Tests', () => {
  describe('TodoList Management Update Component', () => {
    let comp: TodoListUpdateComponent;
    let fixture: ComponentFixture<TodoListUpdateComponent>;
    let activatedRoute: ActivatedRoute;
    let todoListService: TodoListService;
    let userService: UserService;

    beforeEach(() => {
      TestBed.configureTestingModule({
        imports: [HttpClientTestingModule],
        declarations: [TodoListUpdateComponent],
        providers: [FormBuilder, ActivatedRoute],
      })
        .overrideTemplate(TodoListUpdateComponent, '')
        .compileComponents();

      fixture = TestBed.createComponent(TodoListUpdateComponent);
      activatedRoute = TestBed.inject(ActivatedRoute);
      todoListService = TestBed.inject(TodoListService);
      userService = TestBed.inject(UserService);

      comp = fixture.componentInstance;
    });

    describe('ngOnInit', () => {
      it('Should call User query and add missing value', () => {
        const todoList: ITodoList = { id: 456 };
        const user: IUser = { id: 96269 };
        todoList.user = user;

        const userCollection: IUser[] = [{ id: 9702 }];
        jest.spyOn(userService, 'query').mockReturnValue(of(new HttpResponse({ body: userCollection })));
        const additionalUsers = [user];
        const expectedCollection: IUser[] = [...additionalUsers, ...userCollection];
        jest.spyOn(userService, 'addUserToCollectionIfMissing').mockReturnValue(expectedCollection);

        activatedRoute.data = of({ todoList });
        comp.ngOnInit();

        expect(userService.query).toHaveBeenCalled();
        expect(userService.addUserToCollectionIfMissing).toHaveBeenCalledWith(userCollection, ...additionalUsers);
        expect(comp.usersSharedCollection).toEqual(expectedCollection);
      });

      it('Should update editForm', () => {
        const todoList: ITodoList = { id: 456 };
        const user: IUser = { id: 67188 };
        todoList.user = user;

        activatedRoute.data = of({ todoList });
        comp.ngOnInit();

        expect(comp.editForm.value).toEqual(expect.objectContaining(todoList));
        expect(comp.usersSharedCollection).toContain(user);
      });
    });

    describe('save', () => {
      it('Should call update service on save for existing entity', () => {
        // GIVEN
        const saveSubject = new Subject<HttpResponse<TodoList>>();
        const todoList = { id: 123 };
        jest.spyOn(todoListService, 'update').mockReturnValue(saveSubject);
        jest.spyOn(comp, 'previousState');
        activatedRoute.data = of({ todoList });
        comp.ngOnInit();

        // WHEN
        comp.save();
        expect(comp.isSaving).toEqual(true);
        saveSubject.next(new HttpResponse({ body: todoList }));
        saveSubject.complete();

        // THEN
        expect(comp.previousState).toHaveBeenCalled();
        expect(todoListService.update).toHaveBeenCalledWith(todoList);
        expect(comp.isSaving).toEqual(false);
      });

      it('Should call create service on save for new entity', () => {
        // GIVEN
        const saveSubject = new Subject<HttpResponse<TodoList>>();
        const todoList = new TodoList();
        jest.spyOn(todoListService, 'create').mockReturnValue(saveSubject);
        jest.spyOn(comp, 'previousState');
        activatedRoute.data = of({ todoList });
        comp.ngOnInit();

        // WHEN
        comp.save();
        expect(comp.isSaving).toEqual(true);
        saveSubject.next(new HttpResponse({ body: todoList }));
        saveSubject.complete();

        // THEN
        expect(todoListService.create).toHaveBeenCalledWith(todoList);
        expect(comp.isSaving).toEqual(false);
        expect(comp.previousState).toHaveBeenCalled();
      });

      it('Should set isSaving to false on error', () => {
        // GIVEN
        const saveSubject = new Subject<HttpResponse<TodoList>>();
        const todoList = { id: 123 };
        jest.spyOn(todoListService, 'update').mockReturnValue(saveSubject);
        jest.spyOn(comp, 'previousState');
        activatedRoute.data = of({ todoList });
        comp.ngOnInit();

        // WHEN
        comp.save();
        expect(comp.isSaving).toEqual(true);
        saveSubject.error('This is an error!');

        // THEN
        expect(todoListService.update).toHaveBeenCalledWith(todoList);
        expect(comp.isSaving).toEqual(false);
        expect(comp.previousState).not.toHaveBeenCalled();
      });
    });

    describe('Tracking relationships identifiers', () => {
      describe('trackUserById', () => {
        it('Should return tracked User primary key', () => {
          const entity = { id: 123 };
          const trackResult = comp.trackUserById(0, entity);
          expect(trackResult).toEqual(entity.id);
        });
      });
    });
  });
});
