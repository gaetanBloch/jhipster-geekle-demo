import { ITodo } from 'app/entities/todo/todo.model';

export interface ICategory {
  id?: number;
  name?: string;
  description?: string;
  todos?: ITodo[] | null;
}

export class Category implements ICategory {
  constructor(public id?: number, public name?: string, public description?: string, public todos?: ITodo[] | null) {}
}

export function getCategoryIdentifier(category: ICategory): number | undefined {
  return category.id;
}
