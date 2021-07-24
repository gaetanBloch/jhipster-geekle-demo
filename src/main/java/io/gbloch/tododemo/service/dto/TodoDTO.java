package io.gbloch.tododemo.service.dto;

import io.gbloch.tododemo.domain.enumeration.Priority;
import java.io.Serializable;
import java.time.Instant;
import java.util.HashSet;
import java.util.Objects;
import java.util.Set;
import javax.validation.constraints.*;

/**
 * A DTO for the {@link io.gbloch.tododemo.domain.Todo} entity.
 */
public class TodoDTO implements Serializable {

    private Long id;

    @NotNull
    @Size(min = 3)
    private String name;

    @Size(min = 3)
    private String description;

    @NotNull
    private Boolean completed;

    @Min(value = 0)
    private Integer progress;

    @Min(value = 0)
    private Integer order;

    private Priority priority;

    private Instant dueDate;

    private CategoryDTO category;

    private Set<TagDTO> tags = new HashSet<>();

    private TodoListDTO todoList;

    public Long getId() {
        return id;
    }

    public void setId(Long id) {
        this.id = id;
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public String getDescription() {
        return description;
    }

    public void setDescription(String description) {
        this.description = description;
    }

    public Boolean getCompleted() {
        return completed;
    }

    public void setCompleted(Boolean completed) {
        this.completed = completed;
    }

    public Integer getProgress() {
        return progress;
    }

    public void setProgress(Integer progress) {
        this.progress = progress;
    }

    public Integer getOrder() {
        return order;
    }

    public void setOrder(Integer order) {
        this.order = order;
    }

    public Priority getPriority() {
        return priority;
    }

    public void setPriority(Priority priority) {
        this.priority = priority;
    }

    public Instant getDueDate() {
        return dueDate;
    }

    public void setDueDate(Instant dueDate) {
        this.dueDate = dueDate;
    }

    public CategoryDTO getCategory() {
        return category;
    }

    public void setCategory(CategoryDTO category) {
        this.category = category;
    }

    public Set<TagDTO> getTags() {
        return tags;
    }

    public void setTags(Set<TagDTO> tags) {
        this.tags = tags;
    }

    public TodoListDTO getTodoList() {
        return todoList;
    }

    public void setTodoList(TodoListDTO todoList) {
        this.todoList = todoList;
    }

    @Override
    public boolean equals(Object o) {
        if (this == o) {
            return true;
        }
        if (!(o instanceof TodoDTO)) {
            return false;
        }

        TodoDTO todoDTO = (TodoDTO) o;
        if (this.id == null) {
            return false;
        }
        return Objects.equals(this.id, todoDTO.id);
    }

    @Override
    public int hashCode() {
        return Objects.hash(this.id);
    }

    // prettier-ignore
    @Override
    public String toString() {
        return "TodoDTO{" +
            "id=" + getId() +
            ", name='" + getName() + "'" +
            ", description='" + getDescription() + "'" +
            ", completed='" + getCompleted() + "'" +
            ", progress=" + getProgress() +
            ", order=" + getOrder() +
            ", priority='" + getPriority() + "'" +
            ", dueDate='" + getDueDate() + "'" +
            ", category=" + getCategory() +
            ", tags=" + getTags() +
            ", todoList=" + getTodoList() +
            "}";
    }
}
