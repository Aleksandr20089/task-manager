package com.example.service;


import com.example.exception.ResourceNotFoundException;
import com.example.model.Task;
import com.example.repository.TaskRepository;
import org.springframework.stereotype.Service;

import java.util.List;

@Service
public class TaskService {

    private final TaskRepository taskRepository;

    //Внедряю репозиторий с помощью конструктора
    public TaskService(TaskRepository taskRepository) {
        this.taskRepository = taskRepository;
    }
     //ТУт получаю все задачи
    public List<Task> getAllTasks() {
        return taskRepository.findAll();
    }

    // получаем задачу по айди или выдаст ошибку если её нету, тут связано с папкой exception
    public Task getTaskById(Long id) {
        return taskRepository.findById(id)
                .orElseThrow(() -> new ResourceNotFoundException("Задача с id" + id + " не найдена!"));

    }

    public Task createTask(Task task) {
        //тут потом мб добавлю проверку
        return taskRepository.save(task);
    }

    public Task updateTask (Long id, Task taskDetalis){
        Task task = getTaskById(id);// сначала ищем задачу если нету то пдает ошибка

        task.setTitle(taskDetalis.getTitle());
        task.setDescription(taskDetalis.getDescription());
        task.setCompleted(taskDetalis.isCompleted());

        return taskRepository.save(task);
    }

    //удаляю задачу по айди
    public void deleteTask(Long id){
        taskRepository.deleteById(id);
    }
}
