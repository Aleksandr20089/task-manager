package com.example.service;

import com.example.exception.ResourceNotFoundException;
import com.example.model.Task;
import com.example.repository.TaskRepository;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;

import java.util.Optional;

import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.Mockito.*;

@ExtendWith(MockitoExtension.class)
class TaskServiceTest {

    // Создаем фальшивый (mock) репозиторий, чтобы не лезть в реальную базу данных
    @Mock
    private TaskRepository taskRepository;

    // Внедряем этот фейковый репозиторий внутрь нашего реального сервиса
    @InjectMocks
    private TaskService taskService;

    @Test
    void getTaskById_ShouldReturnTask_WhenTaskExists() {
        // 1. Подготовка (Given): настраиваем поведение базы данных
        Task mockTask = new Task();
        mockTask.setId(1L);
        mockTask.setTitle("Купить молоко");

        when(taskRepository.findById(1L)).thenReturn(Optional.of(mockTask));

        // 2. Действие (When): вызываем метод сервиса
        Task result = taskService.getTaskById(1L);

        // 3. Проверка (Then): убеждаемся, что всё отработало корректно
        assertNotNull(result);
        assertEquals("Купить молоко", result.getTitle());
        verify(taskRepository, times(1)).findById(1L);
    }

    @Test
    void getTaskById_ShouldThrowException_WhenTaskNotFound() {
        // 1. Подготовка: говорим, что задача с id 999 не найдена
        when(taskRepository.findById(999L)).thenReturn(Optional.empty());

        // 2 & 3. Действие и Проверка: проверяем, что вылетело наше кастомное исключение
        assertThrows(ResourceNotFoundException.class, () -> {
            taskService.getTaskById(999L);
        });

        verify(taskRepository, times(1)).findById(999L);
    }
}