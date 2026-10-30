package com.example.service;

import com.example.dto.TaskMapper;
import com.example.dto.TaskResponseDto;
import com.example.exception.ResourceNotFoundException;
import com.example.model.Subtask;
import com.example.model.Task;
import com.example.repository.SubtaskRepository;
import com.example.repository.TaskRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.stream.Collectors;

@Service
public class TaskService {

    @Autowired
    private TaskRepository taskRepository;

    @Autowired
    private SubtaskRepository subtaskRepository;

    // Получить все задачи в виде DTO
    public List<TaskResponseDto> getAllTasksDto() {
        return taskRepository.findAll().stream()
                .map(TaskMapper::toDto)
                .collect(Collectors.toList());
    }

    // Получить задачу по ID в виде DTO
    public TaskResponseDto getTaskDtoById(Long id) {
        Task task = taskRepository.findById(id)
                .orElseThrow(() -> new ResourceNotFoundException("Задача с id " + id + " не найдена"));
        return TaskMapper.toDto(task);
    }

    // Создание задачи (принимает Entity сохраняет и возвращает DTO)
    public TaskResponseDto createTask(Task task) {
        Task savedTask = taskRepository.save(task);
        return TaskMapper.toDto(savedTask);
    }

    // Обновление задачи
    public TaskResponseDto updateTask(Long id, Task taskDetails) {
        Task task = taskRepository.findById(id)
                .orElseThrow(() -> new ResourceNotFoundException("Задача с id " + id + " не найдена"));

        task.setTitle(taskDetails.getTitle());
        task.setDescription(taskDetails.getDescription());
        task.setCompleted(taskDetails.isCompleted());
        task.setDeadline(taskDetails.getDeadline());

        Task updatedTask = taskRepository.save(task);
        return TaskMapper.toDto(updatedTask);
    }

    // Удаление задачи
    public void deleteTask(Long id) {
        Task task = taskRepository.findById(id)
                .orElseThrow(() -> new ResourceNotFoundException("Задача с id " + id + " не найдена"));
        taskRepository.delete(task);
    }

    // Добавление подзадачи
    public Subtask addSubtaskToTask(Long taskId, Subtask subtask) {
        Task task = taskRepository.findById(taskId)
                .orElseThrow(() -> new ResourceNotFoundException("Задача с id " + taskId + " не найдена"));
        subtask.setTask(task);
        return subtaskRepository.save(subtask);
    }

    // Обновление статуса подзадачи
    public Subtask updateSubtaskStatus(Long taskId, Long subtaskId, boolean completed) {
        if (!taskRepository.existsById(taskId)) {
            throw new ResourceNotFoundException("Задача с id " + taskId + " не найдена");
        }
        Subtask subtask = subtaskRepository.findById(subtaskId)
                .orElseThrow(() -> new ResourceNotFoundException("Подзадача с id " + subtaskId + " не найдена"));
        subtask.setCompleted(completed);
        return subtaskRepository.save(subtask);
    }
}