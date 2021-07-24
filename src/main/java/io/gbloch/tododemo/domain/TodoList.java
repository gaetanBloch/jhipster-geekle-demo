package io.gbloch.tododemo.domain;

import com.fasterxml.jackson.annotation.JsonIgnoreProperties;
import java.io.Serializable;
import java.util.HashSet;
import java.util.Set;
import javax.persistence.*;
import javax.validation.constraints.*;

/**
 * A TodoList.
 */
@Entity
@Table(name = "todo_list")
public class TodoList implements Serializable {

    private static final long serialVersionUID = 1L;

    @Id
    @GeneratedValue(strategy = GenerationType.SEQUENCE, generator = "sequenceGenerator")
    @SequenceGenerator(name = "sequenceGenerator")
    private Long id;

    @NotNull
    @Size(min = 3)
    @Column(name = "title", nullable = false, unique = true)
    private String title;

    @OneToOne
    @JoinColumn(unique = true)
    private User user;

    @OneToMany(mappedBy = "todoList")
    @JsonIgnoreProperties(value = { "category", "tags", "todoList" }, allowSetters = true)
    private Set<Todo> todos = new HashSet<>();

    // jhipster-needle-entity-add-field - JHipster will add fields here
    public Long getId() {
        return id;
    }

    public void setId(Long id) {
        this.id = id;
    }

    public TodoList id(Long id) {
        this.id = id;
        return this;
    }

    public String getTitle() {
        return this.title;
    }

    public TodoList title(String title) {
        this.title = title;
        return this;
    }

    public void setTitle(String title) {
        this.title = title;
    }

    public User getUser() {
        return this.user;
    }

    public TodoList user(User user) {
        this.setUser(user);
        return this;
    }

    public void setUser(User user) {
        this.user = user;
    }

    public Set<Todo> getTodos() {
        return this.todos;
    }

    public TodoList todos(Set<Todo> todos) {
        this.setTodos(todos);
        return this;
    }

    public TodoList addTodos(Todo todo) {
        this.todos.add(todo);
        todo.setTodoList(this);
        return this;
    }

    public TodoList removeTodos(Todo todo) {
        this.todos.remove(todo);
        todo.setTodoList(null);
        return this;
    }

    public void setTodos(Set<Todo> todos) {
        if (this.todos != null) {
            this.todos.forEach(i -> i.setTodoList(null));
        }
        if (todos != null) {
            todos.forEach(i -> i.setTodoList(this));
        }
        this.todos = todos;
    }

    // jhipster-needle-entity-add-getters-setters - JHipster will add getters and setters here

    @Override
    public boolean equals(Object o) {
        if (this == o) {
            return true;
        }
        if (!(o instanceof TodoList)) {
            return false;
        }
        return id != null && id.equals(((TodoList) o).id);
    }

    @Override
    public int hashCode() {
        // see https://vladmihalcea.com/how-to-implement-equals-and-hashcode-using-the-jpa-entity-identifier/
        return getClass().hashCode();
    }

    // prettier-ignore
    @Override
    public String toString() {
        return "TodoList{" +
            "id=" + getId() +
            ", title='" + getTitle() + "'" +
            "}";
    }
}
