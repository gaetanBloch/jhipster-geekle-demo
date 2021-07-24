package io.gbloch.tododemo.service;

import io.gbloch.tododemo.service.dto.TodoDTO;
import java.util.Optional;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;

/**
 * Service Interface for managing {@link io.gbloch.tododemo.domain.Todo}.
 */
public interface TodoService {
    /**
     * Save a todo.
     *
     * @param todoDTO the entity to save.
     * @return the persisted entity.
     */
    TodoDTO save(TodoDTO todoDTO);

    /**
     * Partially updates a todo.
     *
     * @param todoDTO the entity to update partially.
     * @return the persisted entity.
     */
    Optional<TodoDTO> partialUpdate(TodoDTO todoDTO);

    /**
     * Get all the todos.
     *
     * @param pageable the pagination information.
     * @return the list of entities.
     */
    Page<TodoDTO> findAll(Pageable pageable);

    /**
     * Get all the todos with eager load of many-to-many relationships.
     *
     * @param pageable the pagination information.
     * @return the list of entities.
     */
    Page<TodoDTO> findAllWithEagerRelationships(Pageable pageable);

    /**
     * Get the "id" todo.
     *
     * @param id the id of the entity.
     * @return the entity.
     */
    Optional<TodoDTO> findOne(Long id);

    /**
     * Delete the "id" todo.
     *
     * @param id the id of the entity.
     */
    void delete(Long id);
}
