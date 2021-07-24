import { NgModule } from '@angular/core';
import { RouterModule } from '@angular/router';

@NgModule({
  imports: [
    RouterModule.forChild([
      {
        path: 'todo-list',
        data: { pageTitle: 'todoDemoApp.todoList.home.title' },
        loadChildren: () => import('./todo-list/todo-list.module').then(m => m.TodoListModule),
      },
      {
        path: 'todo',
        data: { pageTitle: 'todoDemoApp.todo.home.title' },
        loadChildren: () => import('./todo/todo.module').then(m => m.TodoModule),
      },
      {
        path: 'category',
        data: { pageTitle: 'todoDemoApp.category.home.title' },
        loadChildren: () => import('./category/category.module').then(m => m.CategoryModule),
      },
      {
        path: 'tag',
        data: { pageTitle: 'todoDemoApp.tag.home.title' },
        loadChildren: () => import('./tag/tag.module').then(m => m.TagModule),
      },
      /* jhipster-needle-add-entity-route - JHipster will add entity modules routes here */
    ]),
  ],
})
export class EntityRoutingModule {}
