package com.example.model;

import jakarta.persistence.*;
import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.Size;

import java.time.LocalDateTime;
import java.util.ArrayList;
import java.util.List;


@Entity
@Table(name = "tasks")
public class Task {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @Column(nullable = false)
    @NotBlank(message = "Название задачи не должно быть пустым")
    @Size(min = 2, max = 100, message = "Название должно быть от 2 до 100 символов")
    private String title;

    private String description;

    private boolean completed;

    private LocalDateTime deadline;

    @OneToMany(mappedBy = "task", cascade = CascadeType.ALL, orphanRemoval = true)
    private List<Subtask> subtasks = new ArrayList<>();

    // Пустой конструктор нужен для Hibernate
    public Task() {
    }

    public LocalDateTime getDeadline(){
        return deadline;
    }
    public void setDeadline(LocalDateTime deadline){
        this.deadline = deadline;
    }

    public List<Subtask> getSubtasks() {
        return subtasks;
    }
    public void setSubtasks(List<Subtask> subtasks) {
        this.subtasks = subtasks;
    }

    // Конструктор для удобного создания задачи
    public Task(String title, String description, boolean completed) {
        this.title = title;
        this.description = description;
        this.completed = completed;
    }

    // Геттеры и сеттеры (чтобы получать и менять поля)
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
}