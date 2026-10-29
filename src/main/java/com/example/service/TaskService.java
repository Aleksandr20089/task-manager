package com.example.service;


import com.example.exception.ResourceNotFoundException;
import com.example.model.Subtask;
import com.example.model.Task;
import com.example.repository.SubtaskRepository;
import com.example.repository.TaskRepository;
import org.springframework.beans.factory.annotation.Autowired;
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

    public Task updateTask(Long id, Task taskDetails) {
        Task task = getTaskById(id);

        task.setTitle(taskDetails.getTitle());
        task.setDescription(taskDetails.getDescription());
        task.setCompleted(taskDetails.isCompleted());
        task.setDeadline(taskDetails.getDeadline());

        // Если нужно обновить и список подзадач целиком (опционально)
        if (taskDetails.getSubtasks() != null) {
            task.setSubtasks(taskDetails.getSubtasks());
        }

        return taskRepository.save(task);
    }

    //удаляю задачу по айди
    public void deleteTask(Long id){
        taskRepository.deleteById(id);
    }

    @Autowired
    private SubtaskRepository subtaskRepository;

    // Метод добавления подзадачи к задаче
    public Subtask addSubtaskToTask(Long taskId, Subtask subtask) {

        // 1. Находим родительскую задачу (если её нет, выбросится наш ResourceNotFoundException)
        Task task = getTaskById(taskId);

        // 2. Связываем подзадачу с задачей
        subtask.setTask(task);

        // 3. Сохраняем подзадачу в БД
        return subtaskRepository.save(subtask);
    }

    // Метод переключения статуса подзадачи (выполнена / не выполнена)
    public Subtask updateSubtaskStatus(Long taskId, Long subtaskId, boolean completed) {
        // 1. Убедимся, что родительская задача существует
        getTaskById(taskId);

        // 2. Ищем подзадачу
        Subtask subtask = subtaskRepository.findById(subtaskId)
                .orElseThrow(() -> new ResourceNotFoundException("Подзадача с id " + subtaskId + " не найдена"));

        // 3. Меняем статус и сохраняем
        subtask.setCompleted(completed);
        return subtaskRepository.save(subtask);
    }
}
