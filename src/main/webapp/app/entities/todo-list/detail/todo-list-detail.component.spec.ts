import { ComponentFixture, TestBed } from '@angular/core/testing';
import { ActivatedRoute } from '@angular/router';
import { of } from 'rxjs';

import { TodoListDetailComponent } from './todo-list-detail.component';

describe('Component Tests', () => {
  describe('TodoList Management Detail Component', () => {
    let comp: TodoListDetailComponent;
    let fixture: ComponentFixture<TodoListDetailComponent>;

    beforeEach(() => {
      TestBed.configureTestingModule({
        declarations: [TodoListDetailComponent],
        providers: [
          {
            provide: ActivatedRoute,
            useValue: { data: of({ todoList: { id: 123 } }) },
          },
        ],
      })
        .overrideTemplate(TodoListDetailComponent, '')
        .compileComponents();
      fixture = TestBed.createComponent(TodoListDetailComponent);
      comp = fixture.componentInstance;
    });

    describe('OnInit', () => {
      it('Should load todoList on init', () => {
        // WHEN
        comp.ngOnInit();

        // THEN
        expect(comp.todoList).toEqual(expect.objectContaining({ id: 123 }));
      });
    });
  });
});
