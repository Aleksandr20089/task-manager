package com.example.service;

import com.example.dto.TaskRequestDto;
import com.fasterxml.jackson.databind.ObjectMapper;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.servlet.AutoConfigureMockMvc;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.http.MediaType;
import org.springframework.test.web.servlet.MockMvc;
import org.springframework.transaction.annotation.Transactional;

import java.time.LocalDateTime;

import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.*;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.*;

@SpringBootTest
@AutoConfigureMockMvc
@Transactional
class TaskControllerTest {

    @Autowired
    private MockMvc mockMvc;

    @Autowired
    private ObjectMapper objectMapper;

    @Test
    @DisplayName("Успешное получение списка задач с пагинацией и сортировкой")
    void shouldReturnTasksWithPagination() throws Exception {
        mockMvc.perform(get("/tasks")
                        .param("page", "0")
                        .param("size", "5")
                        .param("sortBy", "title"))
                .andExpect(status().isOk())
                .andExpect(content().contentType(MediaType.APPLICATION_JSON));
    }

    @Test
    @DisplayName("Успешное создание задачи (201 Created)")
    void shouldCreateTaskSuccessfully() throws Exception {
        TaskRequestDto requestDto = new TaskRequestDto(
                "Сделать лабу по Java",
                "Написать архитектуру и тесты",
                LocalDateTime.now().plusDays(2),
                null
        );

        mockMvc.perform(post("/tasks")
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(objectMapper.writeValueAsString(requestDto)))
                .andExpect(status().isCreated())
                .andExpect(jsonPath("$.title").value("Сделать лабу по Java"))
                .andExpect(jsonPath("$.completed").value(false));
    }

    @Test
    @DisplayName("Ошибка валидации 400 при пустом названии задачи")
    void shouldReturn400WhenTitleIsEmpty() throws Exception {
        TaskRequestDto invalidDto = new TaskRequestDto(
                "",
                "Описание без названия",
                LocalDateTime.now().plusDays(1),
                null
        );

        mockMvc.perform(post("/tasks")
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(objectMapper.writeValueAsString(invalidDto)))
                .andExpect(status().isBadRequest())
                .andExpect(jsonPath("$.status").value(400));
    }

    @Test
    @DisplayName("Ошибка 404 при запросе несуществующей задачи по ID")
    void shouldReturn404WhenTaskNotFound() throws Exception {
        mockMvc.perform(get("/tasks/888888"))
                .andExpect(status().isNotFound())
                .andExpect(jsonPath("$.status").value(404));
    }
}