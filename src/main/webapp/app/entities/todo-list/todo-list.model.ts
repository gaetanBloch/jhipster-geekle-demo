import { IUser } from 'app/entities/user/user.model';
import { ITodo } from 'app/entities/todo/todo.model';

export interface ITodoList {
  id?: number;
  title?: string;
  user?: IUser | null;
  todos?: ITodo[] | null;
}

export class TodoList implements ITodoList {
  constructor(public id?: number, public title?: string, public user?: IUser | null, public todos?: ITodo[] | null) {}
}

export function getTodoListIdentifier(todoList: ITodoList): number | undefined {
  return todoList.id;
}
