package io.gbloch.tododemo.service;

import io.gbloch.tododemo.service.dto.TodoListDTO;
import java.util.List;
import java.util.Optional;

/**
 * Service Interface for managing {@link io.gbloch.tododemo.domain.TodoList}.
 */
public interface TodoListService {
    /**
     * Save a todoList.
     *
     * @param todoListDTO the entity to save.
     * @return the persisted entity.
     */
    TodoListDTO save(TodoListDTO todoListDTO);

    /**
     * Partially updates a todoList.
     *
     * @param todoListDTO the entity to update partially.
     * @return the persisted entity.
     */
    Optional<TodoListDTO> partialUpdate(TodoListDTO todoListDTO);

    /**
     * Get all the todoLists.
     *
     * @return the list of entities.
     */
    List<TodoListDTO> findAll();

    /**
     * Get the "id" todoList.
     *
     * @param id the id of the entity.
     * @return the entity.
     */
    Optional<TodoListDTO> findOne(Long id);

    /**
     * Delete the "id" todoList.
     *
     * @param id the id of the entity.
     */
    void delete(Long id);
}
