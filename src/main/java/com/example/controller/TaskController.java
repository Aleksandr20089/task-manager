package com.example.controller;

import com.example.dto.SubtaskRequestDto;
import com.example.dto.SubtaskResponseDto;
import com.example.dto.TaskRequestDto;
import com.example.dto.TaskResponseDto;
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
@Tag(name = "Управление задачами", description = "Операции для работы с задачами и подзадачами через DTO-рекорды")
public class TaskController {

    @Autowired
    private TaskService taskService;

    @Operation(summary = "Получить список задач с фильтрацией, пагинацией и сортировкой",
            description = "Пример запроса: /tasks?completed=true&page=0&size=10&sortBy=deadline")
    @GetMapping
    public ResponseEntity<List<TaskResponseDto>> getAllTasks(
            @RequestParam(required = false) Boolean completed,
            @RequestParam(defaultValue = "0") int page,
            @RequestParam(defaultValue = "10") int size,
            @RequestParam(defaultValue = "id") String sortBy
    ) {
        return ResponseEntity.ok(taskService.getAllTasksDto(completed, page, size, sortBy));
    }

    @Operation(summary = "Получить задачу по ID")
    @GetMapping("/{id}")
    public ResponseEntity<TaskResponseDto> getTaskById(@PathVariable Long id) {
        return ResponseEntity.ok(taskService.getTaskDtoById(id));
    }

    @Operation(summary = "Создать новую задачу с опциональными подзадачами")
    @PostMapping
    public ResponseEntity<TaskResponseDto> createTask(@Valid @RequestBody TaskRequestDto requestDto) {
        TaskResponseDto createdTask = taskService.createTask(requestDto);
        return new ResponseEntity<>(createdTask, HttpStatus.CREATED);
    }

    @Operation(summary = "Обновить существующую задачу")
    @PutMapping("/{id}")
    public ResponseEntity<TaskResponseDto> updateTask(
            @PathVariable Long id,
            @Valid @RequestBody TaskRequestDto requestDto) {
        TaskResponseDto updatedTask = taskService.updateTask(id, requestDto);
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
    public ResponseEntity<SubtaskResponseDto> addSubtask(
            @PathVariable Long taskId,
            @Valid @RequestBody SubtaskRequestDto requestDto) {
        SubtaskResponseDto createdSubtask = taskService.addSubtaskToTask(taskId, requestDto);
        return new ResponseEntity<>(createdSubtask, HttpStatus.CREATED);
    }

    @Operation(summary = "Обновить статус выполнения подзадачи")
    @PatchMapping("/{taskId}/subtasks/{subtaskId}/status")
    public ResponseEntity<SubtaskResponseDto> updateSubtaskStatus(
            @PathVariable Long taskId,
            @PathVariable Long subtaskId,
            @RequestParam boolean completed) {
        SubtaskResponseDto updatedSubtask = taskService.updateSubtaskStatus(taskId, subtaskId, completed);
        return ResponseEntity.ok(updatedSubtask);
    }
}