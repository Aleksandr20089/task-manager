package com.example.dto;

import com.example.model.Task;
import com.example.model.Subtask;

import java.util.List;
import java.util.stream.Collectors;

public class TaskMapper {

    public static TaskResponseDto toDto(Task task) {
        if (task == null) return null;

        TaskResponseDto dto = new TaskResponseDto();
        dto.setId(task.getId());
        dto.setTitle(task.getTitle());
        dto.setDescription(task.getDescription());
        dto.setCompleted(task.isCompleted());
        dto.setDeadline(task.getDeadline());

        if (task.getSubtasks() != null) {
            List<SubtaskResponseDto> subtaskDtos = task.getSubtasks().stream()
                    .map(TaskMapper::toSubtaskDto)
                    .collect(Collectors.toList());
            dto.setSubtasks(subtaskDtos);
        }

        return dto;
    }

    public static SubtaskResponseDto toSubtaskDto(Subtask subtask) {
        if (subtask == null) return null;

        SubtaskResponseDto dto = new SubtaskResponseDto();
        dto.setId(subtask.getId());
        dto.setTitle(subtask.getTitle());
        dto.setCompleted(subtask.isCompleted());

        return dto;
    }
}