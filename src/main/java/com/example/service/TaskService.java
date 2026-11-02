package com.example.service;

import com.example.dto.*;
import com.example.exception.ResourceNotFoundException;
import com.example.mapper.TaskMapper;
import com.example.model.Subtask;
import com.example.model.Task;
import com.example.repository.SubtaskRepository;
import com.example.repository.TaskRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Pageable;
import org.springframework.data.domain.Sort;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.stream.Collectors;

@Service
public class TaskService {

    @Autowired
    private TaskRepository taskRepository;

    @Autowired
    private SubtaskRepository subtaskRepository;

    // Получить все задачи в виде DTO-рекордов (без параметров)
    public List<TaskResponseDto> getAllTasksDto() {
        return taskRepository.findAll().stream()
                .map(TaskMapper::toDto)
                .collect(Collectors.toList());
    }

    // Получить задачу по ID в виде DTO-рекорда
    public TaskResponseDto getTaskDtoById(Long id) {
        Task task = taskRepository.findById(id)
                .orElseThrow(() -> new ResourceNotFoundException("Задача с id " + id + " не найдена"));
        return TaskMapper.toDto(task);
    }

    // Создание задачи из Request DTO (с опциональными подзадачами)
    public TaskResponseDto createTask(TaskRequestDto requestDto) {
        Task task = new Task();
        task.setTitle(requestDto.title());
        task.setDescription(requestDto.description());
        task.setDeadline(requestDto.deadline());
        task.setCompleted(false);

        // Если клиент передал список подзадач — создаем и привязываем их
        if (requestDto.subtasks() != null) {
            for (SubtaskRequestDto subDto : requestDto.subtasks()) {
                Subtask subtask = new Subtask();
                subtask.setTitle(subDto.title());
                subtask.setCompleted(false);
                task.addSubtask(subtask);
            }
        }

        Task savedTask = taskRepository.save(task);
        return TaskMapper.toDto(savedTask);
    }

    // Обновление задачи
    public TaskResponseDto updateTask(Long id, TaskRequestDto requestDto) {
        Task task = taskRepository.findById(id)
                .orElseThrow(() -> new ResourceNotFoundException("Задача с id " + id + " не найдена"));

        task.setTitle(requestDto.title());
        task.setDescription(requestDto.description());
        task.setDeadline(requestDto.deadline());

        Task updatedTask = taskRepository.save(task);
        return TaskMapper.toDto(updatedTask);
    }

    // Удаление задачи
    public void deleteTask(Long id) {
        Task task = taskRepository.findById(id)
                .orElseThrow(() -> new ResourceNotFoundException("Задача с id " + id + " не найдена"));
        taskRepository.delete(task);
    }

    // Добавление подзадачи к существующей задаче через DTO
    public SubtaskResponseDto addSubtaskToTask(Long taskId, SubtaskRequestDto requestDto) {
        Task task = taskRepository.findById(taskId)
                .orElseThrow(() -> new ResourceNotFoundException("Задача с id " + taskId + " не найдена"));

        Subtask subtask = new Subtask();
        subtask.setTitle(requestDto.title());
        subtask.setCompleted(false);
        subtask.setTask(task);

        Subtask savedSubtask = subtaskRepository.save(subtask);
        return TaskMapper.toSubtaskDto(savedSubtask);
    }

    // Обновление статуса подзадачи
    public SubtaskResponseDto updateSubtaskStatus(Long taskId, Long subtaskId, boolean completed) {
        if (!taskRepository.existsById(taskId)) {
            throw new ResourceNotFoundException("Задача с id " + taskId + " не найдена");
        }
        Subtask subtask = subtaskRepository.findById(subtaskId)
                .orElseThrow(() -> new ResourceNotFoundException("Подзадача с id " + subtaskId + " не найдена"));

        subtask.setCompleted(completed);
        Subtask updatedSubtask = subtaskRepository.save(subtask);
        return TaskMapper.toSubtaskDto(updatedSubtask);
    }

    // Получить задачи с учетом фильтрации по completed, а также пагинации и сортировки
    public List<TaskResponseDto> getAllTasksDto(Boolean completed, int page, int size, String sortBy) {
        Pageable pageable = PageRequest.of(page, size, Sort.by(sortBy));

        Page<Task> taskPage;
        if (completed != null) {
            taskPage = taskRepository.findByCompleted(completed, pageable);
        } else {
            taskPage = taskRepository.findAll(pageable);
        }

        return taskPage.stream()
                .map(TaskMapper::toDto)
                .collect(Collectors.toList());
    }
}