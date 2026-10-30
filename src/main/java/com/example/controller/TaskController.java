package com.example.controller;

import com.example.dto.TaskResponseDto;
import com.example.model.Subtask;
import com.example.model.Task;
import com.example.service.TaskService;
import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.tags.Tag;
import jakarta.validation.Valid;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequestMapping("/tasks")
@Tag(name = "Управление задачами", description = "Операции для работы с задачами и подзадачами")
public class TaskController {

    @Autowired
    private TaskService taskService;

    @Operation(summary = "Получить список всех задач", description = "Возвращает массив всех задач в формате DTO вместе с подзадачами")
    @GetMapping
    public ResponseEntity<List<TaskResponseDto>> getAllTasks() {
        return ResponseEntity.ok(taskService.getAllTasksDto());
    }

    @Operation(summary = "Получить задачу по ID")
    @GetMapping("/{id}")
    public ResponseEntity<TaskResponseDto> getTaskById(@PathVariable Long id) {
        return ResponseEntity.ok(taskService.getTaskDtoById(id));
    }

    @Operation(summary = "Создать новую задачу")
    @PostMapping
    public ResponseEntity<TaskResponseDto> createTask(@Valid @RequestBody Task task) {
        TaskResponseDto createdTask = taskService.createTask(task);
        return new ResponseEntity<>(createdTask, HttpStatus.CREATED);
    }

    @Operation(summary = "Обновить существующую задачу")
    @PutMapping("/{id}")
    public ResponseEntity<TaskResponseDto> updateTask(@PathVariable Long id, @Valid @RequestBody Task taskDetails) {
        TaskResponseDto updatedTask = taskService.updateTask(id, taskDetails);
        return ResponseEntity.ok(updatedTask);
    }

    @Operation(summary = "Удалить задачу по ID")
    @DeleteMapping("/{id}")
    public ResponseEntity<Void> deleteTask(@PathVariable Long id) {
        taskService.deleteTask(id);
        return ResponseEntity.noContent().build();
    }

    @Operation(summary = "Добавить подзадачу к задаче")
    @PostMapping("/{taskId}/subtasks")
    public ResponseEntity<Subtask> addSubtask(
            @PathVariable Long taskId,
            @Valid @RequestBody Subtask subtask) {
        Subtask createdSubtask = taskService.addSubtaskToTask(taskId, subtask);
        return new ResponseEntity<>(createdSubtask, HttpStatus.CREATED);
    }

    @Operation(summary = "Обновить статус выполнения подзадачи")
    @PatchMapping("/{taskId}/subtasks/{subtaskId}/status")
    public ResponseEntity<Subtask> updateSubtaskStatus(
            @PathVariable Long taskId,
            @PathVariable Long subtaskId,
            @RequestParam boolean completed) {
        Subtask updatedSubtask = taskService.updateSubtaskStatus(taskId, subtaskId, completed);
        return ResponseEntity.ok(updatedSubtask);
    }
}