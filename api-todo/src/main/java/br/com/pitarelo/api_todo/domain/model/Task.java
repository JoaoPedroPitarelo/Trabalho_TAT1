package br.com.pitarelo.api_todo.domain.model;

import jakarta.persistence.*;

import java.util.Objects;

@Entity(name = "Task")
@Table(name = "task")
public class Task {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @Column(length = 100)
    private String title;

    @Column(length = 255, nullable = false)
    private String description;

    @Column(nullable = false, columnDefinition = "completed")
    private boolean completed;

    // Construtor vazio para o Hibernate
    public Task() {
    }

    // Outros Construtores -----------

    // Construtor para o createDTO
    public Task(TaskCreateDTO newTask) {
        this.title = newTask.title();
        this.description = newTask.description();
    }

    // Getters e Setters -----------
    public String getTitle() {
        return title;
    }

    public void setTitle(String title) {
        this.title = title;
    }

    public Long getId() {
        return id;
    }

    public void setId(Long id) {
        this.id = id;
    }

    public String getDescription() {
        return description;
    }

    public void setDescription(String description) {
        this.description = description;
    }

    public boolean isCompleted() {
        return completed;
    }

    public void setCompleted(boolean completed) {
        this.completed = completed;
    }

    // Equals e HashCode
    @Override
    public boolean equals(Object o) {
        if (o == null || getClass() != o.getClass()) return false;
        Task task = (Task) o;
        return Objects.equals(id, task.id);
    }

    @Override
    public int hashCode() {
        return Objects.hashCode(id);
    }
}
