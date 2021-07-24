package io.gbloch.tododemo.service.dto;

import static org.assertj.core.api.Assertions.assertThat;

import io.gbloch.tododemo.web.rest.TestUtil;
import org.junit.jupiter.api.Test;

class TodoListDTOTest {

    @Test
    void dtoEqualsVerifier() throws Exception {
        TestUtil.equalsVerifier(TodoListDTO.class);
        TodoListDTO todoListDTO1 = new TodoListDTO();
        todoListDTO1.setId(1L);
        TodoListDTO todoListDTO2 = new TodoListDTO();
        assertThat(todoListDTO1).isNotEqualTo(todoListDTO2);
        todoListDTO2.setId(todoListDTO1.getId());
        assertThat(todoListDTO1).isEqualTo(todoListDTO2);
        todoListDTO2.setId(2L);
        assertThat(todoListDTO1).isNotEqualTo(todoListDTO2);
        todoListDTO1.setId(null);
        assertThat(todoListDTO1).isNotEqualTo(todoListDTO2);
    }
}
