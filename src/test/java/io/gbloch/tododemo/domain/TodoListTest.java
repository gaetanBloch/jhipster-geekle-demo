package io.gbloch.tododemo.domain;

import static org.assertj.core.api.Assertions.assertThat;

import io.gbloch.tododemo.web.rest.TestUtil;
import org.junit.jupiter.api.Test;

class TodoListTest {

    @Test
    void equalsVerifier() throws Exception {
        TestUtil.equalsVerifier(TodoList.class);
        TodoList todoList1 = new TodoList();
        todoList1.setId(1L);
        TodoList todoList2 = new TodoList();
        todoList2.setId(todoList1.getId());
        assertThat(todoList1).isEqualTo(todoList2);
        todoList2.setId(2L);
        assertThat(todoList1).isNotEqualTo(todoList2);
        todoList1.setId(null);
        assertThat(todoList1).isNotEqualTo(todoList2);
    }
}
