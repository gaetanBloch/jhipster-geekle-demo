package io.gbloch.tododemo.service.impl;

import io.gbloch.tododemo.domain.Todo;
import io.gbloch.tododemo.repository.TodoRepository;
import io.gbloch.tododemo.service.TodoService;
import io.gbloch.tododemo.service.dto.TodoDTO;
import io.gbloch.tododemo.service.mapper.TodoMapper;
import java.util.Optional;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

/**
 * Service Implementation for managing {@link Todo}.
 */
@Service
@Transactional
public class TodoServiceImpl implements TodoService {

    private final Logger log = LoggerFactory.getLogger(TodoServiceImpl.class);

    private final TodoRepository todoRepository;

    private final TodoMapper todoMapper;

    public TodoServiceImpl(TodoRepository todoRepository, TodoMapper todoMapper) {
        this.todoRepository = todoRepository;
        this.todoMapper = todoMapper;
    }

    @Override
    public TodoDTO save(TodoDTO todoDTO) {
        log.debug("Request to save Todo : {}", todoDTO);
        Todo todo = todoMapper.toEntity(todoDTO);
        todo = todoRepository.save(todo);
        return todoMapper.toDto(todo);
    }

    @Override
    public Optional<TodoDTO> partialUpdate(TodoDTO todoDTO) {
        log.debug("Request to partially update Todo : {}", todoDTO);

        return todoRepository
            .findById(todoDTO.getId())
            .map(
                existingTodo -> {
                    todoMapper.partialUpdate(existingTodo, todoDTO);

                    return existingTodo;
                }
            )
            .map(todoRepository::save)
            .map(todoMapper::toDto);
    }

    @Override
    @Transactional(readOnly = true)
    public Page<TodoDTO> findAll(Pageable pageable) {
        log.debug("Request to get all Todos");
        return todoRepository.findAll(pageable).map(todoMapper::toDto);
    }

    public Page<TodoDTO> findAllWithEagerRelationships(Pageable pageable) {
        return todoRepository.findAllWithEagerRelationships(pageable).map(todoMapper::toDto);
    }

    @Override
    @Transactional(readOnly = true)
    public Optional<TodoDTO> findOne(Long id) {
        log.debug("Request to get Todo : {}", id);
        return todoRepository.findOneWithEagerRelationships(id).map(todoMapper::toDto);
    }

    @Override
    public void delete(Long id) {
        log.debug("Request to delete Todo : {}", id);
        todoRepository.deleteById(id);
    }
}
