package com.example.dto;

import java.time.LocalDateTime;
import java.util.List;

public record TaskResponseDto(
        Long id,
        String title,
        String description,
        boolean completed,
        LocalDateTime deadline,
        List<SubtaskResponseDto> subtasks
) {}