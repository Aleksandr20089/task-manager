package com.example.controller;

import com.example.model.Subtask;
import com.example.model.Task;
import com.example.service.TaskService;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;
import jakarta.validation.Valid;

import java.util.List;

@RestController
@RequestMapping("/api/tasks")
public class TaskController {

    private final TaskService taskService;

    public TaskController(TaskService taskService) {

        this.taskService = taskService;
    }

    @GetMapping
    public List<Task> getAllTasks() {

        return taskService.getAllTasks();
    }

    @GetMapping("/{id}")
    public ResponseEntity<Task> getTaskById(@PathVariable Long id) {
        Task task = taskService.getTaskById(id);
        return ResponseEntity.ok(task); // Вернет статус 200 OK и задачу в JSON
    }

    @PostMapping
    public Task createTask(@Valid @RequestBody Task task) {

        return taskService.createTask(task);
    }

    @PutMapping("/{id}")
    public ResponseEntity<Task> updateTask(@PathVariable Long id, @Valid @RequestBody Task taskDetails) {
        Task updatedTask = taskService.updateTask(id, taskDetails);
        return ResponseEntity.ok(updatedTask);
    }

    @DeleteMapping("/{id}")
    public void deleteTask(@PathVariable Long id) {

        taskService.deleteTask(id);
    }

    @PostMapping("/{taskId}/subtasks")
    public ResponseEntity<Subtask> addSubtask(
            @PathVariable Long taskId,
            @Valid @RequestBody Subtask subtask) {
        Subtask createdSubtask = taskService.addSubtaskToTask(taskId, subtask);
        return new ResponseEntity<>(createdSubtask, HttpStatus.CREATED);
    }


    // 2. Изменение статуса подзадачи (PATCH /tasks/{taskId}/subtasks/{subtaskId}/status)
    @PatchMapping("/{taskId}/subtasks/{subtaskId}/status")
    public ResponseEntity<Subtask> updateSubtaskStatus(
            @PathVariable Long taskId,
            @PathVariable Long subtaskId,
            @RequestParam boolean completed) {
        Subtask updatedSubtask = taskService.updateSubtaskStatus(taskId, subtaskId, completed);
        return ResponseEntity.ok(updatedSubtask);
    }
}
