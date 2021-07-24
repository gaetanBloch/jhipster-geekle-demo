package io.gbloch.tododemo.service.dto;

import java.io.Serializable;
import java.util.Objects;
import javax.validation.constraints.*;

/**
 * A DTO for the {@link io.gbloch.tododemo.domain.TodoList} entity.
 */
public class TodoListDTO implements Serializable {

    private Long id;

    @NotNull
    @Size(min = 3)
    private String title;

    private UserDTO user;

    public Long getId() {
        return id;
    }

    public void setId(Long id) {
        this.id = id;
    }

    public String getTitle() {
        return title;
    }

    public void setTitle(String title) {
        this.title = title;
    }

    public UserDTO getUser() {
        return user;
    }

    public void setUser(UserDTO user) {
        this.user = user;
    }

    @Override
    public boolean equals(Object o) {
        if (this == o) {
            return true;
        }
        if (!(o instanceof TodoListDTO)) {
            return false;
        }

        TodoListDTO todoListDTO = (TodoListDTO) o;
        if (this.id == null) {
            return false;
        }
        return Objects.equals(this.id, todoListDTO.id);
    }

    @Override
    public int hashCode() {
        return Objects.hash(this.id);
    }

    // prettier-ignore
    @Override
    public String toString() {
        return "TodoListDTO{" +
            "id=" + getId() +
            ", title='" + getTitle() + "'" +
            ", user=" + getUser() +
            "}";
    }
}
