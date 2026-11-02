package com.example.mapper;

import com.example.dto.SubtaskResponseDto;
import com.example.dto.TaskResponseDto;
import com.example.model.Subtask;
import com.example.model.Task;

import java.util.Collections;
import java.util.List;
import java.util.stream.Collectors;

public class TaskMapper {

    public static TaskResponseDto toDto(Task task) {
        if (task == null) return null;

        List<SubtaskResponseDto> subtaskDtos = task.getSubtasks() != null
                ? task.getSubtasks().stream().map(TaskMapper::toSubtaskDto).collect(Collectors.toList())
                : Collections.emptyList();

        return new TaskResponseDto(
                task.getId(),
                task.getTitle(),
                task.getDescription(),
                task.isCompleted(),
                task.getDeadline(),
                subtaskDtos
        );
    }

    public static SubtaskResponseDto toSubtaskDto(Subtask subtask) {
        if (subtask == null) return null;

        return new SubtaskResponseDto(
                subtask.getId(),
                subtask.getTitle(),
                subtask.isCompleted()
        );
    }
}