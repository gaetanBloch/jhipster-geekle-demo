package io.gbloch.tododemo.domain;

import com.fasterxml.jackson.annotation.JsonIgnoreProperties;
import java.io.Serializable;
import java.util.HashSet;
import java.util.Set;
import javax.persistence.*;
import javax.validation.constraints.*;

/**
 * A Category.
 */
@Entity
@Table(name = "category")
public class Category implements Serializable {

    private static final long serialVersionUID = 1L;

    @Id
    @GeneratedValue(strategy = GenerationType.SEQUENCE, generator = "sequenceGenerator")
    @SequenceGenerator(name = "sequenceGenerator")
    private Long id;

    @NotNull
    @Size(min = 3)
    @Column(name = "name", nullable = false, unique = true)
    private String name;

    @NotNull
    @Size(min = 3)
    @Column(name = "description", nullable = false)
    private String description;

    @OneToMany(mappedBy = "category")
    @JsonIgnoreProperties(value = { "category", "todoList", "tags" }, allowSetters = true)
    private Set<Todo> todos = new HashSet<>();

    // jhipster-needle-entity-add-field - JHipster will add fields here
    public Long getId() {
        return id;
    }

    public void setId(Long id) {
        this.id = id;
    }

    public Category id(Long id) {
        this.id = id;
        return this;
    }

    public String getName() {
        return this.name;
    }

    public Category name(String name) {
        this.name = name;
        return this;
    }

    public void setName(String name) {
        this.name = name;
    }

    public String getDescription() {
        return this.description;
    }

    public Category description(String description) {
        this.description = description;
        return this;
    }

    public void setDescription(String description) {
        this.description = description;
    }

    public Set<Todo> getTodos() {
        return this.todos;
    }

    public Category todos(Set<Todo> todos) {
        this.setTodos(todos);
        return this;
    }

    public Category addTodos(Todo todo) {
        this.todos.add(todo);
        todo.setCategory(this);
        return this;
    }

    public Category removeTodos(Todo todo) {
        this.todos.remove(todo);
        todo.setCategory(null);
        return this;
    }

    public void setTodos(Set<Todo> todos) {
        if (this.todos != null) {
            this.todos.forEach(i -> i.setCategory(null));
        }
        if (todos != null) {
            todos.forEach(i -> i.setCategory(this));
        }
        this.todos = todos;
    }

    // jhipster-needle-entity-add-getters-setters - JHipster will add getters and setters here

    @Override
    public boolean equals(Object o) {
        if (this == o) {
            return true;
        }
        if (!(o instanceof Category)) {
            return false;
        }
        return id != null && id.equals(((Category) o).id);
    }

    @Override
    public int hashCode() {
        // see https://vladmihalcea.com/how-to-implement-equals-and-hashcode-using-the-jpa-entity-identifier/
        return getClass().hashCode();
    }

    // prettier-ignore
    @Override
    public String toString() {
        return "Category{" +
            "id=" + getId() +
            ", name='" + getName() + "'" +
            ", description='" + getDescription() + "'" +
            "}";
    }
}
