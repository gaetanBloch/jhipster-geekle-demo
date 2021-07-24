package io.gbloch.tododemo.domain;

import static org.assertj.core.api.Assertions.assertThat;

import io.gbloch.tododemo.web.rest.TestUtil;
import org.junit.jupiter.api.Test;

class TodoTest {

    @Test
    void equalsVerifier() throws Exception {
        TestUtil.equalsVerifier(Todo.class);
        Todo todo1 = new Todo();
        todo1.setId(1L);
        Todo todo2 = new Todo();
        todo2.setId(todo1.getId());
        assertThat(todo1).isEqualTo(todo2);
        todo2.setId(2L);
        assertThat(todo1).isNotEqualTo(todo2);
        todo1.setId(null);
        assertThat(todo1).isNotEqualTo(todo2);
    }
}
