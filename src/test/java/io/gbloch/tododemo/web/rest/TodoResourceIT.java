package io.gbloch.tododemo.web.rest;

import static org.assertj.core.api.Assertions.assertThat;
import static org.hamcrest.Matchers.hasItem;
import static org.mockito.Mockito.*;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.*;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.*;

import io.gbloch.tododemo.IntegrationTest;
import io.gbloch.tododemo.domain.Todo;
import io.gbloch.tododemo.domain.enumeration.Priority;
import io.gbloch.tododemo.repository.TodoRepository;
import io.gbloch.tododemo.service.TodoService;
import io.gbloch.tododemo.service.dto.TodoDTO;
import io.gbloch.tododemo.service.mapper.TodoMapper;
import java.time.Instant;
import java.time.temporal.ChronoUnit;
import java.util.ArrayList;
import java.util.List;
import java.util.Random;
import java.util.concurrent.atomic.AtomicLong;
import javax.persistence.EntityManager;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.servlet.AutoConfigureMockMvc;
import org.springframework.data.domain.PageImpl;
import org.springframework.data.domain.PageRequest;
import org.springframework.http.MediaType;
import org.springframework.security.test.context.support.WithMockUser;
import org.springframework.test.web.servlet.MockMvc;
import org.springframework.transaction.annotation.Transactional;

/**
 * Integration tests for the {@link TodoResource} REST controller.
 */
@IntegrationTest
@ExtendWith(MockitoExtension.class)
@AutoConfigureMockMvc
@WithMockUser
class TodoResourceIT {

    private static final String DEFAULT_NAME = "AAAAAAAAAA";
    private static final String UPDATED_NAME = "BBBBBBBBBB";

    private static final String DEFAULT_DESCRIPTION = "AAAAAAAAAA";
    private static final String UPDATED_DESCRIPTION = "BBBBBBBBBB";

    private static final Boolean DEFAULT_COMPLETED = false;
    private static final Boolean UPDATED_COMPLETED = true;

    private static final Integer DEFAULT_PROGRESS = 0;
    private static final Integer UPDATED_PROGRESS = 1;

    private static final Integer DEFAULT_ORDER = 0;
    private static final Integer UPDATED_ORDER = 1;

    private static final Priority DEFAULT_PRIORITY = Priority.HIGH;
    private static final Priority UPDATED_PRIORITY = Priority.MEDIUM;

    private static final Instant DEFAULT_DUE_DATE = Instant.ofEpochMilli(0L);
    private static final Instant UPDATED_DUE_DATE = Instant.now().truncatedTo(ChronoUnit.MILLIS);

    private static final String ENTITY_API_URL = "/api/todos";
    private static final String ENTITY_API_URL_ID = ENTITY_API_URL + "/{id}";

    private static Random random = new Random();
    private static AtomicLong count = new AtomicLong(random.nextInt() + (2 * Integer.MAX_VALUE));

    @Autowired
    private TodoRepository todoRepository;

    @Mock
    private TodoRepository todoRepositoryMock;

    @Autowired
    private TodoMapper todoMapper;

    @Mock
    private TodoService todoServiceMock;

    @Autowired
    private EntityManager em;

    @Autowired
    private MockMvc restTodoMockMvc;

    private Todo todo;

    /**
     * Create an entity for this test.
     *
     * This is a static method, as tests for other entities might also need it,
     * if they test an entity which requires the current entity.
     */
    public static Todo createEntity(EntityManager em) {
        Todo todo = new Todo()
            .name(DEFAULT_NAME)
            .description(DEFAULT_DESCRIPTION)
            .completed(DEFAULT_COMPLETED)
            .progress(DEFAULT_PROGRESS)
            .order(DEFAULT_ORDER)
            .priority(DEFAULT_PRIORITY)
            .dueDate(DEFAULT_DUE_DATE);
        return todo;
    }

    /**
     * Create an updated entity for this test.
     *
     * This is a static method, as tests for other entities might also need it,
     * if they test an entity which requires the current entity.
     */
    public static Todo createUpdatedEntity(EntityManager em) {
        Todo todo = new Todo()
            .name(UPDATED_NAME)
            .description(UPDATED_DESCRIPTION)
            .completed(UPDATED_COMPLETED)
            .progress(UPDATED_PROGRESS)
            .order(UPDATED_ORDER)
            .priority(UPDATED_PRIORITY)
            .dueDate(UPDATED_DUE_DATE);
        return todo;
    }

    @BeforeEach
    public void initTest() {
        todo = createEntity(em);
    }

    @Test
    @Transactional
    void createTodo() throws Exception {
        int databaseSizeBeforeCreate = todoRepository.findAll().size();
        // Create the Todo
        TodoDTO todoDTO = todoMapper.toDto(todo);
        restTodoMockMvc
            .perform(post(ENTITY_API_URL).contentType(MediaType.APPLICATION_JSON).content(TestUtil.convertObjectToJsonBytes(todoDTO)))
            .andExpect(status().isCreated());

        // Validate the Todo in the database
        List<Todo> todoList = todoRepository.findAll();
        assertThat(todoList).hasSize(databaseSizeBeforeCreate + 1);
        Todo testTodo = todoList.get(todoList.size() - 1);
        assertThat(testTodo.getName()).isEqualTo(DEFAULT_NAME);
        assertThat(testTodo.getDescription()).isEqualTo(DEFAULT_DESCRIPTION);
        assertThat(testTodo.getCompleted()).isEqualTo(DEFAULT_COMPLETED);
        assertThat(testTodo.getProgress()).isEqualTo(DEFAULT_PROGRESS);
        assertThat(testTodo.getOrder()).isEqualTo(DEFAULT_ORDER);
        assertThat(testTodo.getPriority()).isEqualTo(DEFAULT_PRIORITY);
        assertThat(testTodo.getDueDate()).isEqualTo(DEFAULT_DUE_DATE);
    }

    @Test
    @Transactional
    void createTodoWithExistingId() throws Exception {
        // Create the Todo with an existing ID
        todo.setId(1L);
        TodoDTO todoDTO = todoMapper.toDto(todo);

        int databaseSizeBeforeCreate = todoRepository.findAll().size();

        // An entity with an existing ID cannot be created, so this API call must fail
        restTodoMockMvc
            .perform(post(ENTITY_API_URL).contentType(MediaType.APPLICATION_JSON).content(TestUtil.convertObjectToJsonBytes(todoDTO)))
            .andExpect(status().isBadRequest());

        // Validate the Todo in the database
        List<Todo> todoList = todoRepository.findAll();
        assertThat(todoList).hasSize(databaseSizeBeforeCreate);
    }

    @Test
    @Transactional
    void checkNameIsRequired() throws Exception {
        int databaseSizeBeforeTest = todoRepository.findAll().size();
        // set the field null
        todo.setName(null);

        // Create the Todo, which fails.
        TodoDTO todoDTO = todoMapper.toDto(todo);

        restTodoMockMvc
            .perform(post(ENTITY_API_URL).contentType(MediaType.APPLICATION_JSON).content(TestUtil.convertObjectToJsonBytes(todoDTO)))
            .andExpect(status().isBadRequest());

        List<Todo> todoList = todoRepository.findAll();
        assertThat(todoList).hasSize(databaseSizeBeforeTest);
    }

    @Test
    @Transactional
    void checkCompletedIsRequired() throws Exception {
        int databaseSizeBeforeTest = todoRepository.findAll().size();
        // set the field null
        todo.setCompleted(null);

        // Create the Todo, which fails.
        TodoDTO todoDTO = todoMapper.toDto(todo);

        restTodoMockMvc
            .perform(post(ENTITY_API_URL).contentType(MediaType.APPLICATION_JSON).content(TestUtil.convertObjectToJsonBytes(todoDTO)))
            .andExpect(status().isBadRequest());

        List<Todo> todoList = todoRepository.findAll();
        assertThat(todoList).hasSize(databaseSizeBeforeTest);
    }

    @Test
    @Transactional
    void getAllTodos() throws Exception {
        // Initialize the database
        todoRepository.saveAndFlush(todo);

        // Get all the todoList
        restTodoMockMvc
            .perform(get(ENTITY_API_URL + "?sort=id,desc"))
            .andExpect(status().isOk())
            .andExpect(content().contentType(MediaType.APPLICATION_JSON_VALUE))
            .andExpect(jsonPath("$.[*].id").value(hasItem(todo.getId().intValue())))
            .andExpect(jsonPath("$.[*].name").value(hasItem(DEFAULT_NAME)))
            .andExpect(jsonPath("$.[*].description").value(hasItem(DEFAULT_DESCRIPTION)))
            .andExpect(jsonPath("$.[*].completed").value(hasItem(DEFAULT_COMPLETED.booleanValue())))
            .andExpect(jsonPath("$.[*].progress").value(hasItem(DEFAULT_PROGRESS)))
            .andExpect(jsonPath("$.[*].order").value(hasItem(DEFAULT_ORDER)))
            .andExpect(jsonPath("$.[*].priority").value(hasItem(DEFAULT_PRIORITY.toString())))
            .andExpect(jsonPath("$.[*].dueDate").value(hasItem(DEFAULT_DUE_DATE.toString())));
    }

    @SuppressWarnings({ "unchecked" })
    void getAllTodosWithEagerRelationshipsIsEnabled() throws Exception {
        when(todoServiceMock.findAllWithEagerRelationships(any())).thenReturn(new PageImpl(new ArrayList<>()));

        restTodoMockMvc.perform(get(ENTITY_API_URL + "?eagerload=true")).andExpect(status().isOk());

        verify(todoServiceMock, times(1)).findAllWithEagerRelationships(any());
    }

    @SuppressWarnings({ "unchecked" })
    void getAllTodosWithEagerRelationshipsIsNotEnabled() throws Exception {
        when(todoServiceMock.findAllWithEagerRelationships(any())).thenReturn(new PageImpl(new ArrayList<>()));

        restTodoMockMvc.perform(get(ENTITY_API_URL + "?eagerload=true")).andExpect(status().isOk());

        verify(todoServiceMock, times(1)).findAllWithEagerRelationships(any());
    }

    @Test
    @Transactional
    void getTodo() throws Exception {
        // Initialize the database
        todoRepository.saveAndFlush(todo);

        // Get the todo
        restTodoMockMvc
            .perform(get(ENTITY_API_URL_ID, todo.getId()))
            .andExpect(status().isOk())
            .andExpect(content().contentType(MediaType.APPLICATION_JSON_VALUE))
            .andExpect(jsonPath("$.id").value(todo.getId().intValue()))
            .andExpect(jsonPath("$.name").value(DEFAULT_NAME))
            .andExpect(jsonPath("$.description").value(DEFAULT_DESCRIPTION))
            .andExpect(jsonPath("$.completed").value(DEFAULT_COMPLETED.booleanValue()))
            .andExpect(jsonPath("$.progress").value(DEFAULT_PROGRESS))
            .andExpect(jsonPath("$.order").value(DEFAULT_ORDER))
            .andExpect(jsonPath("$.priority").value(DEFAULT_PRIORITY.toString()))
            .andExpect(jsonPath("$.dueDate").value(DEFAULT_DUE_DATE.toString()));
    }

    @Test
    @Transactional
    void getNonExistingTodo() throws Exception {
        // Get the todo
        restTodoMockMvc.perform(get(ENTITY_API_URL_ID, Long.MAX_VALUE)).andExpect(status().isNotFound());
    }

    @Test
    @Transactional
    void putNewTodo() throws Exception {
        // Initialize the database
        todoRepository.saveAndFlush(todo);

        int databaseSizeBeforeUpdate = todoRepository.findAll().size();

        // Update the todo
        Todo updatedTodo = todoRepository.findById(todo.getId()).get();
        // Disconnect from session so that the updates on updatedTodo are not directly saved in db
        em.detach(updatedTodo);
        updatedTodo
            .name(UPDATED_NAME)
            .description(UPDATED_DESCRIPTION)
            .completed(UPDATED_COMPLETED)
            .progress(UPDATED_PROGRESS)
            .order(UPDATED_ORDER)
            .priority(UPDATED_PRIORITY)
            .dueDate(UPDATED_DUE_DATE);
        TodoDTO todoDTO = todoMapper.toDto(updatedTodo);

        restTodoMockMvc
            .perform(
                put(ENTITY_API_URL_ID, todoDTO.getId())
                    .contentType(MediaType.APPLICATION_JSON)
                    .content(TestUtil.convertObjectToJsonBytes(todoDTO))
            )
            .andExpect(status().isOk());

        // Validate the Todo in the database
        List<Todo> todoList = todoRepository.findAll();
        assertThat(todoList).hasSize(databaseSizeBeforeUpdate);
        Todo testTodo = todoList.get(todoList.size() - 1);
        assertThat(testTodo.getName()).isEqualTo(UPDATED_NAME);
        assertThat(testTodo.getDescription()).isEqualTo(UPDATED_DESCRIPTION);
        assertThat(testTodo.getCompleted()).isEqualTo(UPDATED_COMPLETED);
        assertThat(testTodo.getProgress()).isEqualTo(UPDATED_PROGRESS);
        assertThat(testTodo.getOrder()).isEqualTo(UPDATED_ORDER);
        assertThat(testTodo.getPriority()).isEqualTo(UPDATED_PRIORITY);
        assertThat(testTodo.getDueDate()).isEqualTo(UPDATED_DUE_DATE);
    }

    @Test
    @Transactional
    void putNonExistingTodo() throws Exception {
        int databaseSizeBeforeUpdate = todoRepository.findAll().size();
        todo.setId(count.incrementAndGet());

        // Create the Todo
        TodoDTO todoDTO = todoMapper.toDto(todo);

        // If the entity doesn't have an ID, it will throw BadRequestAlertException
        restTodoMockMvc
            .perform(
                put(ENTITY_API_URL_ID, todoDTO.getId())
                    .contentType(MediaType.APPLICATION_JSON)
                    .content(TestUtil.convertObjectToJsonBytes(todoDTO))
            )
            .andExpect(status().isBadRequest());

        // Validate the Todo in the database
        List<Todo> todoList = todoRepository.findAll();
        assertThat(todoList).hasSize(databaseSizeBeforeUpdate);
    }

    @Test
    @Transactional
    void putWithIdMismatchTodo() throws Exception {
        int databaseSizeBeforeUpdate = todoRepository.findAll().size();
        todo.setId(count.incrementAndGet());

        // Create the Todo
        TodoDTO todoDTO = todoMapper.toDto(todo);

        // If url ID doesn't match entity ID, it will throw BadRequestAlertException
        restTodoMockMvc
            .perform(
                put(ENTITY_API_URL_ID, count.incrementAndGet())
                    .contentType(MediaType.APPLICATION_JSON)
                    .content(TestUtil.convertObjectToJsonBytes(todoDTO))
            )
            .andExpect(status().isBadRequest());

        // Validate the Todo in the database
        List<Todo> todoList = todoRepository.findAll();
        assertThat(todoList).hasSize(databaseSizeBeforeUpdate);
    }

    @Test
    @Transactional
    void putWithMissingIdPathParamTodo() throws Exception {
        int databaseSizeBeforeUpdate = todoRepository.findAll().size();
        todo.setId(count.incrementAndGet());

        // Create the Todo
        TodoDTO todoDTO = todoMapper.toDto(todo);

        // If url ID doesn't match entity ID, it will throw BadRequestAlertException
        restTodoMockMvc
            .perform(put(ENTITY_API_URL).contentType(MediaType.APPLICATION_JSON).content(TestUtil.convertObjectToJsonBytes(todoDTO)))
            .andExpect(status().isMethodNotAllowed());

        // Validate the Todo in the database
        List<Todo> todoList = todoRepository.findAll();
        assertThat(todoList).hasSize(databaseSizeBeforeUpdate);
    }

    @Test
    @Transactional
    void partialUpdateTodoWithPatch() throws Exception {
        // Initialize the database
        todoRepository.saveAndFlush(todo);

        int databaseSizeBeforeUpdate = todoRepository.findAll().size();

        // Update the todo using partial update
        Todo partialUpdatedTodo = new Todo();
        partialUpdatedTodo.setId(todo.getId());

        partialUpdatedTodo.name(UPDATED_NAME).description(UPDATED_DESCRIPTION).priority(UPDATED_PRIORITY);

        restTodoMockMvc
            .perform(
                patch(ENTITY_API_URL_ID, partialUpdatedTodo.getId())
                    .contentType("application/merge-patch+json")
                    .content(TestUtil.convertObjectToJsonBytes(partialUpdatedTodo))
            )
            .andExpect(status().isOk());

        // Validate the Todo in the database
        List<Todo> todoList = todoRepository.findAll();
        assertThat(todoList).hasSize(databaseSizeBeforeUpdate);
        Todo testTodo = todoList.get(todoList.size() - 1);
        assertThat(testTodo.getName()).isEqualTo(UPDATED_NAME);
        assertThat(testTodo.getDescription()).isEqualTo(UPDATED_DESCRIPTION);
        assertThat(testTodo.getCompleted()).isEqualTo(DEFAULT_COMPLETED);
        assertThat(testTodo.getProgress()).isEqualTo(DEFAULT_PROGRESS);
        assertThat(testTodo.getOrder()).isEqualTo(DEFAULT_ORDER);
        assertThat(testTodo.getPriority()).isEqualTo(UPDATED_PRIORITY);
        assertThat(testTodo.getDueDate()).isEqualTo(DEFAULT_DUE_DATE);
    }

    @Test
    @Transactional
    void fullUpdateTodoWithPatch() throws Exception {
        // Initialize the database
        todoRepository.saveAndFlush(todo);

        int databaseSizeBeforeUpdate = todoRepository.findAll().size();

        // Update the todo using partial update
        Todo partialUpdatedTodo = new Todo();
        partialUpdatedTodo.setId(todo.getId());

        partialUpdatedTodo
            .name(UPDATED_NAME)
            .description(UPDATED_DESCRIPTION)
            .completed(UPDATED_COMPLETED)
            .progress(UPDATED_PROGRESS)
            .order(UPDATED_ORDER)
            .priority(UPDATED_PRIORITY)
            .dueDate(UPDATED_DUE_DATE);

        restTodoMockMvc
            .perform(
                patch(ENTITY_API_URL_ID, partialUpdatedTodo.getId())
                    .contentType("application/merge-patch+json")
                    .content(TestUtil.convertObjectToJsonBytes(partialUpdatedTodo))
            )
            .andExpect(status().isOk());

        // Validate the Todo in the database
        List<Todo> todoList = todoRepository.findAll();
        assertThat(todoList).hasSize(databaseSizeBeforeUpdate);
        Todo testTodo = todoList.get(todoList.size() - 1);
        assertThat(testTodo.getName()).isEqualTo(UPDATED_NAME);
        assertThat(testTodo.getDescription()).isEqualTo(UPDATED_DESCRIPTION);
        assertThat(testTodo.getCompleted()).isEqualTo(UPDATED_COMPLETED);
        assertThat(testTodo.getProgress()).isEqualTo(UPDATED_PROGRESS);
        assertThat(testTodo.getOrder()).isEqualTo(UPDATED_ORDER);
        assertThat(testTodo.getPriority()).isEqualTo(UPDATED_PRIORITY);
        assertThat(testTodo.getDueDate()).isEqualTo(UPDATED_DUE_DATE);
    }

    @Test
    @Transactional
    void patchNonExistingTodo() throws Exception {
        int databaseSizeBeforeUpdate = todoRepository.findAll().size();
        todo.setId(count.incrementAndGet());

        // Create the Todo
        TodoDTO todoDTO = todoMapper.toDto(todo);

        // If the entity doesn't have an ID, it will throw BadRequestAlertException
        restTodoMockMvc
            .perform(
                patch(ENTITY_API_URL_ID, todoDTO.getId())
                    .contentType("application/merge-patch+json")
                    .content(TestUtil.convertObjectToJsonBytes(todoDTO))
            )
            .andExpect(status().isBadRequest());

        // Validate the Todo in the database
        List<Todo> todoList = todoRepository.findAll();
        assertThat(todoList).hasSize(databaseSizeBeforeUpdate);
    }

    @Test
    @Transactional
    void patchWithIdMismatchTodo() throws Exception {
        int databaseSizeBeforeUpdate = todoRepository.findAll().size();
        todo.setId(count.incrementAndGet());

        // Create the Todo
        TodoDTO todoDTO = todoMapper.toDto(todo);

        // If url ID doesn't match entity ID, it will throw BadRequestAlertException
        restTodoMockMvc
            .perform(
                patch(ENTITY_API_URL_ID, count.incrementAndGet())
                    .contentType("application/merge-patch+json")
                    .content(TestUtil.convertObjectToJsonBytes(todoDTO))
            )
            .andExpect(status().isBadRequest());

        // Validate the Todo in the database
        List<Todo> todoList = todoRepository.findAll();
        assertThat(todoList).hasSize(databaseSizeBeforeUpdate);
    }

    @Test
    @Transactional
    void patchWithMissingIdPathParamTodo() throws Exception {
        int databaseSizeBeforeUpdate = todoRepository.findAll().size();
        todo.setId(count.incrementAndGet());

        // Create the Todo
        TodoDTO todoDTO = todoMapper.toDto(todo);

        // If url ID doesn't match entity ID, it will throw BadRequestAlertException
        restTodoMockMvc
            .perform(patch(ENTITY_API_URL).contentType("application/merge-patch+json").content(TestUtil.convertObjectToJsonBytes(todoDTO)))
            .andExpect(status().isMethodNotAllowed());

        // Validate the Todo in the database
        List<Todo> todoList = todoRepository.findAll();
        assertThat(todoList).hasSize(databaseSizeBeforeUpdate);
    }

    @Test
    @Transactional
    void deleteTodo() throws Exception {
        // Initialize the database
        todoRepository.saveAndFlush(todo);

        int databaseSizeBeforeDelete = todoRepository.findAll().size();

        // Delete the todo
        restTodoMockMvc
            .perform(delete(ENTITY_API_URL_ID, todo.getId()).accept(MediaType.APPLICATION_JSON))
            .andExpect(status().isNoContent());

        // Validate the database contains one less item
        List<Todo> todoList = todoRepository.findAll();
        assertThat(todoList).hasSize(databaseSizeBeforeDelete - 1);
    }
}
