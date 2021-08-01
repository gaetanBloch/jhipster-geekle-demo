package io.gbloch.tododemo.domain;

import com.fasterxml.jackson.annotation.JsonIgnoreProperties;
import io.gbloch.tododemo.domain.enumeration.Priority;
import java.io.Serializable;
import java.time.Instant;
import java.util.HashSet;
import java.util.Set;
import javax.persistence.*;
import javax.validation.constraints.*;

/**
 * Todo Task
 */
@Entity
@Table(name = "todo")
public class Todo implements Serializable {

    private static final long serialVersionUID = 1L;

    @Id
    @GeneratedValue(strategy = GenerationType.SEQUENCE, generator = "sequenceGenerator")
    @SequenceGenerator(name = "sequenceGenerator")
    private Long id;

    /**
     * Todo task name
     */
    @NotNull
    @Size(min = 3)
    @Column(name = "name", nullable = false)
    private String name;

    @Size(min = 3)
    @Column(name = "description")
    private String description;

    @NotNull
    @Column(name = "completed", nullable = false)
    private Boolean completed;

    @Min(value = 0)
    @Column(name = "progress")
    private Integer progress;

    @Min(value = 0)
    @Column(name = "jhi_order")
    private Integer order;

    @Enumerated(EnumType.STRING)
    @Column(name = "priority")
    private Priority priority;

    @Column(name = "due_date")
    private Instant dueDate;

    @ManyToOne
    private Category category;

    @ManyToMany
    @JoinTable(name = "rel_todo__tag", joinColumns = @JoinColumn(name = "todo_id"), inverseJoinColumns = @JoinColumn(name = "tag_id"))
    @JsonIgnoreProperties(value = { "todos" }, allowSetters = true)
    private Set<Tag> tags = new HashSet<>();

    @ManyToOne
    @JsonIgnoreProperties(value = { "user", "todos" }, allowSetters = true)
    private TodoList todoList;

    // jhipster-needle-entity-add-field - JHipster will add fields here
    public Long getId() {
        return id;
    }

    public void setId(Long id) {
        this.id = id;
    }

    public Todo id(Long id) {
        this.id = id;
        return this;
    }

    public String getName() {
        return this.name;
    }

    public Todo name(String name) {
        this.name = name;
        return this;
    }

    public void setName(String name) {
        this.name = name;
    }

    public String getDescription() {
        return this.description;
    }

    public Todo description(String description) {
        this.description = description;
        return this;
    }

    public void setDescription(String description) {
        this.description = description;
    }

    public Boolean getCompleted() {
        return this.completed;
    }

    public Todo completed(Boolean completed) {
        this.completed = completed;
        return this;
    }

    public void setCompleted(Boolean completed) {
        this.completed = completed;
    }

    public Integer getProgress() {
        return this.progress;
    }

    public Todo progress(Integer progress) {
        this.progress = progress;
        return this;
    }

    public void setProgress(Integer progress) {
        this.progress = progress;
    }

    public Integer getOrder() {
        return this.order;
    }

    public Todo order(Integer order) {
        this.order = order;
        return this;
    }

    public void setOrder(Integer order) {
        this.order = order;
    }

    public Priority getPriority() {
        return this.priority;
    }

    public Todo priority(Priority priority) {
        this.priority = priority;
        return this;
    }

    public void setPriority(Priority priority) {
        this.priority = priority;
    }

    public Instant getDueDate() {
        return this.dueDate;
    }

    public Todo dueDate(Instant dueDate) {
        this.dueDate = dueDate;
        return this;
    }

    public void setDueDate(Instant dueDate) {
        this.dueDate = dueDate;
    }

    public Category getCategory() {
        return this.category;
    }

    public Todo category(Category category) {
        this.setCategory(category);
        return this;
    }

    public void setCategory(Category category) {
        this.category = category;
    }

    public Set<Tag> getTags() {
        return this.tags;
    }

    public Todo tags(Set<Tag> tags) {
        this.setTags(tags);
        return this;
    }

    public Todo addTag(Tag tag) {
        this.tags.add(tag);
        tag.getTodos().add(this);
        return this;
    }

    public Todo removeTag(Tag tag) {
        this.tags.remove(tag);
        tag.getTodos().remove(this);
        return this;
    }

    public void setTags(Set<Tag> tags) {
        this.tags = tags;
    }

    public TodoList getTodoList() {
        return this.todoList;
    }

    public Todo todoList(TodoList todoList) {
        this.setTodoList(todoList);
        return this;
    }

    public void setTodoList(TodoList todoList) {
        this.todoList = todoList;
    }

    // jhipster-needle-entity-add-getters-setters - JHipster will add getters and setters here

    @Override
    public boolean equals(Object o) {
        if (this == o) {
            return true;
        }
        if (!(o instanceof Todo)) {
            return false;
        }
        return id != null && id.equals(((Todo) o).id);
    }

    @Override
    public int hashCode() {
        // see https://vladmihalcea.com/how-to-implement-equals-and-hashcode-using-the-jpa-entity-identifier/
        return getClass().hashCode();
    }

    // prettier-ignore
    @Override
    public String toString() {
        return "Todo{" +
            "id=" + getId() +
            ", name='" + getName() + "'" +
            ", description='" + getDescription() + "'" +
            ", completed='" + getCompleted() + "'" +
            ", progress=" + getProgress() +
            ", order=" + getOrder() +
            ", priority='" + getPriority() + "'" +
            ", dueDate='" + getDueDate() + "'" +
            "}";
    }
}
