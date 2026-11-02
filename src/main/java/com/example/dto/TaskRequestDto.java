package com.example.dto;

import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.Size;
import java.time.LocalDateTime;
import java.util.List;

// принимает сервер при создании и обновлении задачи это всё в одном месте
public record TaskRequestDto(
        @NotBlank(message = "Название задачи не может быть пустым")
        @Size(min = 3, max = 150, message = "Название должно быть от 3 до 150 символов")
        String title,

        @Size(max = 500, message = "Описание не должно превышать 500 символов")
        String description,

        LocalDateTime deadline,

        // Опционально: можно передать подзадачи прямо при создании задачи
        List<SubtaskRequestDto> subtasks
) {}