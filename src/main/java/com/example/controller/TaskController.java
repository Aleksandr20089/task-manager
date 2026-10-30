package com.example.controller;

import com.example.dto.TaskResponseDto;
import com.example.model.Subtask;
import com.example.model.Task;
import com.example.service.TaskService;
import jakarta.validation.Valid;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequestMapping("/tasks")
public class TaskController {

    @Autowired
    private TaskService taskService;

    @GetMapping
    public ResponseEntity<List<TaskResponseDto>> getAllTasks() {
        return ResponseEntity.ok(taskService.getAllTasksDto());
    }

    @GetMapping("/{id}")
    public ResponseEntity<TaskResponseDto> getTaskById(@PathVariable Long id) {
        return ResponseEntity.ok(taskService.getTaskDtoById(id));
    }

    @PostMapping
    public ResponseEntity<TaskResponseDto> createTask(@Valid @RequestBody Task task) {
        TaskResponseDto createdTask = taskService.createTask(task);
        return new ResponseEntity<>(createdTask, HttpStatus.CREATED);
    }

    @PutMapping("/{id}")
    public ResponseEntity<TaskResponseDto> updateTask(@PathVariable Long id, @Valid @RequestBody Task taskDetails) {
        TaskResponseDto updatedTask = taskService.updateTask(id, taskDetails);
        return ResponseEntity.ok(updatedTask);
    }

    @DeleteMapping("/{id}")
    public ResponseEntity<Void> deleteTask(@PathVariable Long id) {
        taskService.deleteTask(id);
        return ResponseEntity.noContent().build();
    }

    @PostMapping("/{taskId}/subtasks")
    public ResponseEntity<Subtask> addSubtask(
            @PathVariable Long taskId,
            @Valid @RequestBody Subtask subtask) {
        Subtask createdSubtask = taskService.addSubtaskToTask(taskId, subtask);
        return new ResponseEntity<>(createdSubtask, HttpStatus.CREATED);
    }

    @PatchMapping("/{taskId}/subtasks/{subtaskId}/status")
    public ResponseEntity<Subtask> updateSubtaskStatus(
            @PathVariable Long taskId,
            @PathVariable Long subtaskId,
            @RequestParam boolean completed) {
        Subtask updatedSubtask = taskService.updateSubtaskStatus(taskId, subtaskId, completed);
        return ResponseEntity.ok(updatedSubtask);
    }
}