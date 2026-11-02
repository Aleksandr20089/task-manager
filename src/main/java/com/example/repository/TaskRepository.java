package com.example.repository;

import com.example.model.Task;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

@Repository
public interface TaskRepository extends JpaRepository<Task, Long> {

    // Поиск задач по статусу выполнения (completed) с поддержкой пагинации и сортировки
    Page<Task> findByCompleted(boolean completed, Pageable pageable);
}