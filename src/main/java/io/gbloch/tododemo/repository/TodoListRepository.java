package io.gbloch.tododemo.repository;

import io.gbloch.tododemo.domain.TodoList;
import org.springframework.data.jpa.repository.*;
import org.springframework.stereotype.Repository;

/**
 * Spring Data SQL repository for the TodoList entity.
 */
@SuppressWarnings("unused")
@Repository
public interface TodoListRepository extends JpaRepository<TodoList, Long> {}
