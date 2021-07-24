package io.gbloch.tododemo.web.rest;

import static org.assertj.core.api.Assertions.assertThat;
import static org.hamcrest.Matchers.hasItem;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.*;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.*;

import io.gbloch.tododemo.IntegrationTest;
import io.gbloch.tododemo.domain.TodoList;
import io.gbloch.tododemo.repository.TodoListRepository;
import io.gbloch.tododemo.service.dto.TodoListDTO;
import io.gbloch.tododemo.service.mapper.TodoListMapper;
import java.util.List;
import java.util.Random;
import java.util.concurrent.atomic.AtomicLong;
import javax.persistence.EntityManager;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.servlet.AutoConfigureMockMvc;
import org.springframework.http.MediaType;
import org.springframework.security.test.context.support.WithMockUser;
import org.springframework.test.web.servlet.MockMvc;
import org.springframework.transaction.annotation.Transactional;

/**
 * Integration tests for the {@link TodoListResource} REST controller.
 */
@IntegrationTest
@AutoConfigureMockMvc
@WithMockUser
class TodoListResourceIT {

    private static final String DEFAULT_TITLE = "AAAAAAAAAA";
    private static final String UPDATED_TITLE = "BBBBBBBBBB";

    private static final String ENTITY_API_URL = "/api/todo-lists";
    private static final String ENTITY_API_URL_ID = ENTITY_API_URL + "/{id}";

    private static Random random = new Random();
    private static AtomicLong count = new AtomicLong(random.nextInt() + (2 * Integer.MAX_VALUE));

    @Autowired
    private TodoListRepository todoListRepository;

    @Autowired
    private TodoListMapper todoListMapper;

    @Autowired
    private EntityManager em;

    @Autowired
    private MockMvc restTodoListMockMvc;

    private TodoList todoList;

    /**
     * Create an entity for this test.
     *
     * This is a static method, as tests for other entities might also need it,
     * if they test an entity which requires the current entity.
     */
    public static TodoList createEntity(EntityManager em) {
        TodoList todoList = new TodoList().title(DEFAULT_TITLE);
        return todoList;
    }

    /**
     * Create an updated entity for this test.
     *
     * This is a static method, as tests for other entities might also need it,
     * if they test an entity which requires the current entity.
     */
    public static TodoList createUpdatedEntity(EntityManager em) {
        TodoList todoList = new TodoList().title(UPDATED_TITLE);
        return todoList;
    }

    @BeforeEach
    public void initTest() {
        todoList = createEntity(em);
    }

    @Test
    @Transactional
    void createTodoList() throws Exception {
        int databaseSizeBeforeCreate = todoListRepository.findAll().size();
        // Create the TodoList
        TodoListDTO todoListDTO = todoListMapper.toDto(todoList);
        restTodoListMockMvc
            .perform(post(ENTITY_API_URL).contentType(MediaType.APPLICATION_JSON).content(TestUtil.convertObjectToJsonBytes(todoListDTO)))
            .andExpect(status().isCreated());

        // Validate the TodoList in the database
        List<TodoList> todoListList = todoListRepository.findAll();
        assertThat(todoListList).hasSize(databaseSizeBeforeCreate + 1);
        TodoList testTodoList = todoListList.get(todoListList.size() - 1);
        assertThat(testTodoList.getTitle()).isEqualTo(DEFAULT_TITLE);
    }

    @Test
    @Transactional
    void createTodoListWithExistingId() throws Exception {
        // Create the TodoList with an existing ID
        todoList.setId(1L);
        TodoListDTO todoListDTO = todoListMapper.toDto(todoList);

        int databaseSizeBeforeCreate = todoListRepository.findAll().size();

        // An entity with an existing ID cannot be created, so this API call must fail
        restTodoListMockMvc
            .perform(post(ENTITY_API_URL).contentType(MediaType.APPLICATION_JSON).content(TestUtil.convertObjectToJsonBytes(todoListDTO)))
            .andExpect(status().isBadRequest());

        // Validate the TodoList in the database
        List<TodoList> todoListList = todoListRepository.findAll();
        assertThat(todoListList).hasSize(databaseSizeBeforeCreate);
    }

    @Test
    @Transactional
    void checkTitleIsRequired() throws Exception {
        int databaseSizeBeforeTest = todoListRepository.findAll().size();
        // set the field null
        todoList.setTitle(null);

        // Create the TodoList, which fails.
        TodoListDTO todoListDTO = todoListMapper.toDto(todoList);

        restTodoListMockMvc
            .perform(post(ENTITY_API_URL).contentType(MediaType.APPLICATION_JSON).content(TestUtil.convertObjectToJsonBytes(todoListDTO)))
            .andExpect(status().isBadRequest());

        List<TodoList> todoListList = todoListRepository.findAll();
        assertThat(todoListList).hasSize(databaseSizeBeforeTest);
    }

    @Test
    @Transactional
    void getAllTodoLists() throws Exception {
        // Initialize the database
        todoListRepository.saveAndFlush(todoList);

        // Get all the todoListList
        restTodoListMockMvc
            .perform(get(ENTITY_API_URL + "?sort=id,desc"))
            .andExpect(status().isOk())
            .andExpect(content().contentType(MediaType.APPLICATION_JSON_VALUE))
            .andExpect(jsonPath("$.[*].id").value(hasItem(todoList.getId().intValue())))
            .andExpect(jsonPath("$.[*].title").value(hasItem(DEFAULT_TITLE)));
    }

    @Test
    @Transactional
    void getTodoList() throws Exception {
        // Initialize the database
        todoListRepository.saveAndFlush(todoList);

        // Get the todoList
        restTodoListMockMvc
            .perform(get(ENTITY_API_URL_ID, todoList.getId()))
            .andExpect(status().isOk())
            .andExpect(content().contentType(MediaType.APPLICATION_JSON_VALUE))
            .andExpect(jsonPath("$.id").value(todoList.getId().intValue()))
            .andExpect(jsonPath("$.title").value(DEFAULT_TITLE));
    }

    @Test
    @Transactional
    void getNonExistingTodoList() throws Exception {
        // Get the todoList
        restTodoListMockMvc.perform(get(ENTITY_API_URL_ID, Long.MAX_VALUE)).andExpect(status().isNotFound());
    }

    @Test
    @Transactional
    void putNewTodoList() throws Exception {
        // Initialize the database
        todoListRepository.saveAndFlush(todoList);

        int databaseSizeBeforeUpdate = todoListRepository.findAll().size();

        // Update the todoList
        TodoList updatedTodoList = todoListRepository.findById(todoList.getId()).get();
        // Disconnect from session so that the updates on updatedTodoList are not directly saved in db
        em.detach(updatedTodoList);
        updatedTodoList.title(UPDATED_TITLE);
        TodoListDTO todoListDTO = todoListMapper.toDto(updatedTodoList);

        restTodoListMockMvc
            .perform(
                put(ENTITY_API_URL_ID, todoListDTO.getId())
                    .contentType(MediaType.APPLICATION_JSON)
                    .content(TestUtil.convertObjectToJsonBytes(todoListDTO))
            )
            .andExpect(status().isOk());

        // Validate the TodoList in the database
        List<TodoList> todoListList = todoListRepository.findAll();
        assertThat(todoListList).hasSize(databaseSizeBeforeUpdate);
        TodoList testTodoList = todoListList.get(todoListList.size() - 1);
        assertThat(testTodoList.getTitle()).isEqualTo(UPDATED_TITLE);
    }

    @Test
    @Transactional
    void putNonExistingTodoList() throws Exception {
        int databaseSizeBeforeUpdate = todoListRepository.findAll().size();
        todoList.setId(count.incrementAndGet());

        // Create the TodoList
        TodoListDTO todoListDTO = todoListMapper.toDto(todoList);

        // If the entity doesn't have an ID, it will throw BadRequestAlertException
        restTodoListMockMvc
            .perform(
                put(ENTITY_API_URL_ID, todoListDTO.getId())
                    .contentType(MediaType.APPLICATION_JSON)
                    .content(TestUtil.convertObjectToJsonBytes(todoListDTO))
            )
            .andExpect(status().isBadRequest());

        // Validate the TodoList in the database
        List<TodoList> todoListList = todoListRepository.findAll();
        assertThat(todoListList).hasSize(databaseSizeBeforeUpdate);
    }

    @Test
    @Transactional
    void putWithIdMismatchTodoList() throws Exception {
        int databaseSizeBeforeUpdate = todoListRepository.findAll().size();
        todoList.setId(count.incrementAndGet());

        // Create the TodoList
        TodoListDTO todoListDTO = todoListMapper.toDto(todoList);

        // If url ID doesn't match entity ID, it will throw BadRequestAlertException
        restTodoListMockMvc
            .perform(
                put(ENTITY_API_URL_ID, count.incrementAndGet())
                    .contentType(MediaType.APPLICATION_JSON)
                    .content(TestUtil.convertObjectToJsonBytes(todoListDTO))
            )
            .andExpect(status().isBadRequest());

        // Validate the TodoList in the database
        List<TodoList> todoListList = todoListRepository.findAll();
        assertThat(todoListList).hasSize(databaseSizeBeforeUpdate);
    }

    @Test
    @Transactional
    void putWithMissingIdPathParamTodoList() throws Exception {
        int databaseSizeBeforeUpdate = todoListRepository.findAll().size();
        todoList.setId(count.incrementAndGet());

        // Create the TodoList
        TodoListDTO todoListDTO = todoListMapper.toDto(todoList);

        // If url ID doesn't match entity ID, it will throw BadRequestAlertException
        restTodoListMockMvc
            .perform(put(ENTITY_API_URL).contentType(MediaType.APPLICATION_JSON).content(TestUtil.convertObjectToJsonBytes(todoListDTO)))
            .andExpect(status().isMethodNotAllowed());

        // Validate the TodoList in the database
        List<TodoList> todoListList = todoListRepository.findAll();
        assertThat(todoListList).hasSize(databaseSizeBeforeUpdate);
    }

    @Test
    @Transactional
    void partialUpdateTodoListWithPatch() throws Exception {
        // Initialize the database
        todoListRepository.saveAndFlush(todoList);

        int databaseSizeBeforeUpdate = todoListRepository.findAll().size();

        // Update the todoList using partial update
        TodoList partialUpdatedTodoList = new TodoList();
        partialUpdatedTodoList.setId(todoList.getId());

        restTodoListMockMvc
            .perform(
                patch(ENTITY_API_URL_ID, partialUpdatedTodoList.getId())
                    .contentType("application/merge-patch+json")
                    .content(TestUtil.convertObjectToJsonBytes(partialUpdatedTodoList))
            )
            .andExpect(status().isOk());

        // Validate the TodoList in the database
        List<TodoList> todoListList = todoListRepository.findAll();
        assertThat(todoListList).hasSize(databaseSizeBeforeUpdate);
        TodoList testTodoList = todoListList.get(todoListList.size() - 1);
        assertThat(testTodoList.getTitle()).isEqualTo(DEFAULT_TITLE);
    }

    @Test
    @Transactional
    void fullUpdateTodoListWithPatch() throws Exception {
        // Initialize the database
        todoListRepository.saveAndFlush(todoList);

        int databaseSizeBeforeUpdate = todoListRepository.findAll().size();

        // Update the todoList using partial update
        TodoList partialUpdatedTodoList = new TodoList();
        partialUpdatedTodoList.setId(todoList.getId());

        partialUpdatedTodoList.title(UPDATED_TITLE);

        restTodoListMockMvc
            .perform(
                patch(ENTITY_API_URL_ID, partialUpdatedTodoList.getId())
                    .contentType("application/merge-patch+json")
                    .content(TestUtil.convertObjectToJsonBytes(partialUpdatedTodoList))
            )
            .andExpect(status().isOk());

        // Validate the TodoList in the database
        List<TodoList> todoListList = todoListRepository.findAll();
        assertThat(todoListList).hasSize(databaseSizeBeforeUpdate);
        TodoList testTodoList = todoListList.get(todoListList.size() - 1);
        assertThat(testTodoList.getTitle()).isEqualTo(UPDATED_TITLE);
    }

    @Test
    @Transactional
    void patchNonExistingTodoList() throws Exception {
        int databaseSizeBeforeUpdate = todoListRepository.findAll().size();
        todoList.setId(count.incrementAndGet());

        // Create the TodoList
        TodoListDTO todoListDTO = todoListMapper.toDto(todoList);

        // If the entity doesn't have an ID, it will throw BadRequestAlertException
        restTodoListMockMvc
            .perform(
                patch(ENTITY_API_URL_ID, todoListDTO.getId())
                    .contentType("application/merge-patch+json")
                    .content(TestUtil.convertObjectToJsonBytes(todoListDTO))
            )
            .andExpect(status().isBadRequest());

        // Validate the TodoList in the database
        List<TodoList> todoListList = todoListRepository.findAll();
        assertThat(todoListList).hasSize(databaseSizeBeforeUpdate);
    }

    @Test
    @Transactional
    void patchWithIdMismatchTodoList() throws Exception {
        int databaseSizeBeforeUpdate = todoListRepository.findAll().size();
        todoList.setId(count.incrementAndGet());

        // Create the TodoList
        TodoListDTO todoListDTO = todoListMapper.toDto(todoList);

        // If url ID doesn't match entity ID, it will throw BadRequestAlertException
        restTodoListMockMvc
            .perform(
                patch(ENTITY_API_URL_ID, count.incrementAndGet())
                    .contentType("application/merge-patch+json")
                    .content(TestUtil.convertObjectToJsonBytes(todoListDTO))
            )
            .andExpect(status().isBadRequest());

        // Validate the TodoList in the database
        List<TodoList> todoListList = todoListRepository.findAll();
        assertThat(todoListList).hasSize(databaseSizeBeforeUpdate);
    }

    @Test
    @Transactional
    void patchWithMissingIdPathParamTodoList() throws Exception {
        int databaseSizeBeforeUpdate = todoListRepository.findAll().size();
        todoList.setId(count.incrementAndGet());

        // Create the TodoList
        TodoListDTO todoListDTO = todoListMapper.toDto(todoList);

        // If url ID doesn't match entity ID, it will throw BadRequestAlertException
        restTodoListMockMvc
            .perform(
                patch(ENTITY_API_URL).contentType("application/merge-patch+json").content(TestUtil.convertObjectToJsonBytes(todoListDTO))
            )
            .andExpect(status().isMethodNotAllowed());

        // Validate the TodoList in the database
        List<TodoList> todoListList = todoListRepository.findAll();
        assertThat(todoListList).hasSize(databaseSizeBeforeUpdate);
    }

    @Test
    @Transactional
    void deleteTodoList() throws Exception {
        // Initialize the database
        todoListRepository.saveAndFlush(todoList);

        int databaseSizeBeforeDelete = todoListRepository.findAll().size();

        // Delete the todoList
        restTodoListMockMvc
            .perform(delete(ENTITY_API_URL_ID, todoList.getId()).accept(MediaType.APPLICATION_JSON))
            .andExpect(status().isNoContent());

        // Validate the database contains one less item
        List<TodoList> todoListList = todoListRepository.findAll();
        assertThat(todoListList).hasSize(databaseSizeBeforeDelete - 1);
    }
}
