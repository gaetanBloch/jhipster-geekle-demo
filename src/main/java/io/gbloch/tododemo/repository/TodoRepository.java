package io.gbloch.tododemo.repository;

import io.gbloch.tododemo.domain.Todo;
import java.util.List;
import java.util.Optional;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.repository.*;
import org.springframework.data.repository.query.Param;
import org.springframework.stereotype.Repository;

/**
 * Spring Data SQL repository for the Todo entity.
 */
@Repository
public interface TodoRepository extends JpaRepository<Todo, Long> {
    @Query(
        value = "select distinct todo from Todo todo left join fetch todo.tags",
        countQuery = "select count(distinct todo) from Todo todo"
    )
    Page<Todo> findAllWithEagerRelationships(Pageable pageable);

    @Query("select distinct todo from Todo todo left join fetch todo.tags")
    List<Todo> findAllWithEagerRelationships();

    @Query("select todo from Todo todo left join fetch todo.tags where todo.id =:id")
    Optional<Todo> findOneWithEagerRelationships(@Param("id") Long id);
}
