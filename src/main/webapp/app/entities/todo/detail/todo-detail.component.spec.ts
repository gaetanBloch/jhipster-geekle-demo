import { ComponentFixture, TestBed } from '@angular/core/testing';
import { ActivatedRoute } from '@angular/router';
import { of } from 'rxjs';

import { TodoDetailComponent } from './todo-detail.component';

describe('Component Tests', () => {
  describe('Todo Management Detail Component', () => {
    let comp: TodoDetailComponent;
    let fixture: ComponentFixture<TodoDetailComponent>;

    beforeEach(() => {
      TestBed.configureTestingModule({
        declarations: [TodoDetailComponent],
        providers: [
          {
            provide: ActivatedRoute,
            useValue: { data: of({ todo: { id: 123 } }) },
          },
        ],
      })
        .overrideTemplate(TodoDetailComponent, '')
        .compileComponents();
      fixture = TestBed.createComponent(TodoDetailComponent);
      comp = fixture.componentInstance;
    });

    describe('OnInit', () => {
      it('Should load todo on init', () => {
        // WHEN
        comp.ngOnInit();

        // THEN
        expect(comp.todo).toEqual(expect.objectContaining({ id: 123 }));
      });
    });
  });
});
