package com.example.dto;

import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.Size;

//это для созданияи изменение подзадачи

public record SubtaskRequestDto(
        @NotBlank(message = "Название подзадачи не должно быть пустым")
        @Size(min = 2, max = 100, message = "Название подзадачи должно быть от 2 до 100 символов")
        String title
) {}