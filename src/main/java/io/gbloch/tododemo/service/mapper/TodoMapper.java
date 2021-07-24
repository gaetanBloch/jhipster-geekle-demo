package io.gbloch.tododemo.service.mapper;

import io.gbloch.tododemo.domain.*;
import io.gbloch.tododemo.service.dto.TodoDTO;
import java.util.Set;
import org.mapstruct.*;

/**
 * Mapper for the entity {@link Todo} and its DTO {@link TodoDTO}.
 */
@Mapper(componentModel = "spring", uses = { CategoryMapper.class, TagMapper.class, TodoListMapper.class })
public interface TodoMapper extends EntityMapper<TodoDTO, Todo> {
    @Mapping(target = "category", source = "category", qualifiedByName = "name")
    @Mapping(target = "tags", source = "tags", qualifiedByName = "nameSet")
    @Mapping(target = "todoList", source = "todoList", qualifiedByName = "id")
    TodoDTO toDto(Todo s);

    @Mapping(target = "removeTag", ignore = true)
    Todo toEntity(TodoDTO todoDTO);
}
