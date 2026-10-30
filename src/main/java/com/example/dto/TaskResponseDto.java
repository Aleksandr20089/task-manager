package com.example.dto;

import java.time.LocalDateTime;
import java.util.List;

public class TaskResponseDto {
    private Long id;
    private String title;
    private String description;
    private boolean completed;
    private LocalDateTime deadline;
    private List<SubtaskResponseDto> subtasks;

    // Конструкторы
    public TaskResponseDto() {}

    // Геттеры и сеттеры
    public Long getId() { return id; }
    public void setId(Long id) { this.id = id; }

    public String getTitle() { return title; }
    public void setTitle(String title) { this.title = title; }

    public String getDescription() { return description; }
    public void setDescription(String description) { this.description = description; }

    public boolean isCompleted() { return completed; }
    public void setCompleted(boolean completed) { this.completed = completed; }

    public LocalDateTime getDeadline() { return deadline; }
    public void setDeadline(LocalDateTime deadline) { this.deadline = deadline; }

    public List<SubtaskResponseDto> getSubtasks() { return subtasks; }
    public void setSubtasks(List<SubtaskResponseDto> subtasks) { this.subtasks = subtasks; }
}