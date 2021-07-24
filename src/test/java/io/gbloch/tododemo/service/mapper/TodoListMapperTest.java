package io.gbloch.tododemo.service.mapper;

import static org.assertj.core.api.Assertions.assertThat;

import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;

class TodoListMapperTest {

    private TodoListMapper todoListMapper;

    @BeforeEach
    public void setUp() {
        todoListMapper = new TodoListMapperImpl();
    }
}
