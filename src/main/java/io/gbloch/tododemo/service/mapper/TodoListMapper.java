package io.gbloch.tododemo.service.mapper;

import io.gbloch.tododemo.domain.*;
import io.gbloch.tododemo.service.dto.TodoListDTO;
import org.mapstruct.*;

/**
 * Mapper for the entity {@link TodoList} and its DTO {@link TodoListDTO}.
 */
@Mapper(componentModel = "spring", uses = { UserMapper.class })
public interface TodoListMapper extends EntityMapper<TodoListDTO, TodoList> {
    @Mapping(target = "user", source = "user", qualifiedByName = "login")
    TodoListDTO toDto(TodoList s);

    @Named("id")
    @BeanMapping(ignoreByDefault = true)
    @Mapping(target = "id", source = "id")
    TodoListDTO toDtoId(TodoList todoList);
}
