package io.gbloch.tododemo.web.rest;

import io.gbloch.tododemo.repository.TodoListRepository;
import io.gbloch.tododemo.service.TodoListService;
import io.gbloch.tododemo.service.dto.TodoListDTO;
import io.gbloch.tododemo.web.rest.errors.BadRequestAlertException;
import java.net.URI;
import java.net.URISyntaxException;
import java.util.List;
import java.util.Objects;
import java.util.Optional;
import javax.validation.Valid;
import javax.validation.constraints.NotNull;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;
import tech.jhipster.web.util.HeaderUtil;
import tech.jhipster.web.util.ResponseUtil;

/**
 * REST controller for managing {@link io.gbloch.tododemo.domain.TodoList}.
 */
@RestController
@RequestMapping("/api")
public class TodoListResource {

    private final Logger log = LoggerFactory.getLogger(TodoListResource.class);

    private static final String ENTITY_NAME = "todoList";

    @Value("${jhipster.clientApp.name}")
    private String applicationName;

    private final TodoListService todoListService;

    private final TodoListRepository todoListRepository;

    public TodoListResource(TodoListService todoListService, TodoListRepository todoListRepository) {
        this.todoListService = todoListService;
        this.todoListRepository = todoListRepository;
    }

    /**
     * {@code POST  /todo-lists} : Create a new todoList.
     *
     * @param todoListDTO the todoListDTO to create.
     * @return the {@link ResponseEntity} with status {@code 201 (Created)} and with body the new todoListDTO, or with status {@code 400 (Bad Request)} if the todoList has already an ID.
     * @throws URISyntaxException if the Location URI syntax is incorrect.
     */
    @PostMapping("/todo-lists")
    public ResponseEntity<TodoListDTO> createTodoList(@Valid @RequestBody TodoListDTO todoListDTO) throws URISyntaxException {
        log.debug("REST request to save TodoList : {}", todoListDTO);
        if (todoListDTO.getId() != null) {
            throw new BadRequestAlertException("A new todoList cannot already have an ID", ENTITY_NAME, "idexists");
        }
        TodoListDTO result = todoListService.save(todoListDTO);
        return ResponseEntity
            .created(new URI("/api/todo-lists/" + result.getId()))
            .headers(HeaderUtil.createEntityCreationAlert(applicationName, true, ENTITY_NAME, result.getId().toString()))
            .body(result);
    }

    /**
     * {@code PUT  /todo-lists/:id} : Updates an existing todoList.
     *
     * @param id the id of the todoListDTO to save.
     * @param todoListDTO the todoListDTO to update.
     * @return the {@link ResponseEntity} with status {@code 200 (OK)} and with body the updated todoListDTO,
     * or with status {@code 400 (Bad Request)} if the todoListDTO is not valid,
     * or with status {@code 500 (Internal Server Error)} if the todoListDTO couldn't be updated.
     * @throws URISyntaxException if the Location URI syntax is incorrect.
     */
    @PutMapping("/todo-lists/{id}")
    public ResponseEntity<TodoListDTO> updateTodoList(
        @PathVariable(value = "id", required = false) final Long id,
        @Valid @RequestBody TodoListDTO todoListDTO
    ) throws URISyntaxException {
        log.debug("REST request to update TodoList : {}, {}", id, todoListDTO);
        if (todoListDTO.getId() == null) {
            throw new BadRequestAlertException("Invalid id", ENTITY_NAME, "idnull");
        }
        if (!Objects.equals(id, todoListDTO.getId())) {
            throw new BadRequestAlertException("Invalid ID", ENTITY_NAME, "idinvalid");
        }

        if (!todoListRepository.existsById(id)) {
            throw new BadRequestAlertException("Entity not found", ENTITY_NAME, "idnotfound");
        }

        TodoListDTO result = todoListService.save(todoListDTO);
        return ResponseEntity
            .ok()
            .headers(HeaderUtil.createEntityUpdateAlert(applicationName, true, ENTITY_NAME, todoListDTO.getId().toString()))
            .body(result);
    }

    /**
     * {@code PATCH  /todo-lists/:id} : Partial updates given fields of an existing todoList, field will ignore if it is null
     *
     * @param id the id of the todoListDTO to save.
     * @param todoListDTO the todoListDTO to update.
     * @return the {@link ResponseEntity} with status {@code 200 (OK)} and with body the updated todoListDTO,
     * or with status {@code 400 (Bad Request)} if the todoListDTO is not valid,
     * or with status {@code 404 (Not Found)} if the todoListDTO is not found,
     * or with status {@code 500 (Internal Server Error)} if the todoListDTO couldn't be updated.
     * @throws URISyntaxException if the Location URI syntax is incorrect.
     */
    @PatchMapping(value = "/todo-lists/{id}", consumes = "application/merge-patch+json")
    public ResponseEntity<TodoListDTO> partialUpdateTodoList(
        @PathVariable(value = "id", required = false) final Long id,
        @NotNull @RequestBody TodoListDTO todoListDTO
    ) throws URISyntaxException {
        log.debug("REST request to partial update TodoList partially : {}, {}", id, todoListDTO);
        if (todoListDTO.getId() == null) {
            throw new BadRequestAlertException("Invalid id", ENTITY_NAME, "idnull");
        }
        if (!Objects.equals(id, todoListDTO.getId())) {
            throw new BadRequestAlertException("Invalid ID", ENTITY_NAME, "idinvalid");
        }

        if (!todoListRepository.existsById(id)) {
            throw new BadRequestAlertException("Entity not found", ENTITY_NAME, "idnotfound");
        }

        Optional<TodoListDTO> result = todoListService.partialUpdate(todoListDTO);

        return ResponseUtil.wrapOrNotFound(
            result,
            HeaderUtil.createEntityUpdateAlert(applicationName, true, ENTITY_NAME, todoListDTO.getId().toString())
        );
    }

    /**
     * {@code GET  /todo-lists} : get all the todoLists.
     *
     * @return the {@link ResponseEntity} with status {@code 200 (OK)} and the list of todoLists in body.
     */
    @GetMapping("/todo-lists")
    public List<TodoListDTO> getAllTodoLists() {
        log.debug("REST request to get all TodoLists");
        return todoListService.findAll();
    }

    /**
     * {@code GET  /todo-lists/:id} : get the "id" todoList.
     *
     * @param id the id of the todoListDTO to retrieve.
     * @return the {@link ResponseEntity} with status {@code 200 (OK)} and with body the todoListDTO, or with status {@code 404 (Not Found)}.
     */
    @GetMapping("/todo-lists/{id}")
    public ResponseEntity<TodoListDTO> getTodoList(@PathVariable Long id) {
        log.debug("REST request to get TodoList : {}", id);
        Optional<TodoListDTO> todoListDTO = todoListService.findOne(id);
        return ResponseUtil.wrapOrNotFound(todoListDTO);
    }

    /**
     * {@code DELETE  /todo-lists/:id} : delete the "id" todoList.
     *
     * @param id the id of the todoListDTO to delete.
     * @return the {@link ResponseEntity} with status {@code 204 (NO_CONTENT)}.
     */
    @DeleteMapping("/todo-lists/{id}")
    public ResponseEntity<Void> deleteTodoList(@PathVariable Long id) {
        log.debug("REST request to delete TodoList : {}", id);
        todoListService.delete(id);
        return ResponseEntity
            .noContent()
            .headers(HeaderUtil.createEntityDeletionAlert(applicationName, true, ENTITY_NAME, id.toString()))
            .build();
    }
}
