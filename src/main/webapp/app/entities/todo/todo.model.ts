import * as dayjs from 'dayjs';
import { ICategory } from 'app/entities/category/category.model';
import { ITodoList } from 'app/entities/todo-list/todo-list.model';
import { ITag } from 'app/entities/tag/tag.model';
import { Priority } from 'app/entities/enumerations/priority.model';

export interface ITodo {
  id?: number;
  name?: string;
  description?: string | null;
  completed?: boolean;
  progress?: number | null;
  order?: number | null;
  priority?: Priority | null;
  dueDate?: dayjs.Dayjs | null;
  category?: ICategory | null;
  todoList?: ITodoList | null;
  tags?: ITag[] | null;
}

export class Todo implements ITodo {
  constructor(
    public id?: number,
    public name?: string,
    public description?: string | null,
    public completed?: boolean,
    public progress?: number | null,
    public order?: number | null,
    public priority?: Priority | null,
    public dueDate?: dayjs.Dayjs | null,
    public category?: ICategory | null,
    public todoList?: ITodoList | null,
    public tags?: ITag[] | null
  ) {
    this.completed = this.completed ?? false;
  }
}

export function getTodoIdentifier(todo: ITodo): number | undefined {
  return todo.id;
}
