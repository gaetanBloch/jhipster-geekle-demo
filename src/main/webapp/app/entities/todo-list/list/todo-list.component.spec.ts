import { ComponentFixture, TestBed } from '@angular/core/testing';
import { HttpHeaders, HttpResponse } from '@angular/common/http';
import { HttpClientTestingModule } from '@angular/common/http/testing';
import { of } from 'rxjs';

import { TodoListService } from '../service/todo-list.service';

import { TodoListComponent } from './todo-list.component';

describe('Component Tests', () => {
  describe('TodoList Management Component', () => {
    let comp: TodoListComponent;
    let fixture: ComponentFixture<TodoListComponent>;
    let service: TodoListService;

    beforeEach(() => {
      TestBed.configureTestingModule({
        imports: [HttpClientTestingModule],
        declarations: [TodoListComponent],
      })
        .overrideTemplate(TodoListComponent, '')
        .compileComponents();

      fixture = TestBed.createComponent(TodoListComponent);
      comp = fixture.componentInstance;
      service = TestBed.inject(TodoListService);

      const headers = new HttpHeaders().append('link', 'link;link');
      jest.spyOn(service, 'query').mockReturnValue(
        of(
          new HttpResponse({
            body: [{ id: 123 }],
            headers,
          })
        )
      );
    });

    it('Should call load all on init', () => {
      // WHEN
      comp.ngOnInit();

      // THEN
      expect(service.query).toHaveBeenCalled();
      expect(comp.todoLists?.[0]).toEqual(expect.objectContaining({ id: 123 }));
    });
  });
});
