package io.gbloch.tododemo.service.mapper;

import io.gbloch.tododemo.domain.*;
import io.gbloch.tododemo.service.dto.TodoDTO;
import java.util.Set;
import org.mapstruct.*;

/**
 * Mapper for the entity {@link Todo} and its DTO {@link TodoDTO}.
 */
@Mapper(componentModel = "spring", uses = { CategoryMapper.class, TodoListMapper.class, TagMapper.class })
public interface TodoMapper extends EntityMapper<TodoDTO, Todo> {
    @Mapping(target = "category", source = "category", qualifiedByName = "name")
    @Mapping(target = "todoList", source = "todoList", qualifiedByName = "title")
    @Mapping(target = "tags", source = "tags", qualifiedByName = "nameSet")
    TodoDTO toDto(Todo s);

    @Mapping(target = "removeTags", ignore = true)
    Todo toEntity(TodoDTO todoDTO);
}
