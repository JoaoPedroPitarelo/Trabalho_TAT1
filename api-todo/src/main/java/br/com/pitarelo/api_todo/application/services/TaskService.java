package br.com.pitarelo.api_todo.application.services;

import br.com.pitarelo.api_todo.domain.model.Task;
import br.com.pitarelo.api_todo.infraestruture.persistence.TaskJPARepository;
import br.com.pitarelo.api_todo.domain.model.TaskUpdateDTO;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.List;

@Service
public class TaskService {

    @Autowired
    private TaskJPARepository taskRepository;

    // Create
    public void createTask(Task task) {
        task.setCompleted(false);
        taskRepository.save(task);
    }

    // ById
    public Task getById(Long taskId) {
        Task task = taskRepository.findById(taskId).orElse(null);
        return task;
    }

    // GetAll
    public List<Task> getTasks() {
        return taskRepository.findByCompletedFalse();
    }

    // Update
    public void updateTask(Task task, TaskUpdateDTO modifiedTask) {
        task.setCompleted(modifiedTask.completed());
        task.setTitle(modifiedTask.title() != null ? modifiedTask.title() : task.getTitle());
        task.setDescription(modifiedTask.description() != null ? modifiedTask.description() : task.getDescription());

        taskRepository.save(task);
    }

    // Delete - (lógica)
    public void deleteTask(Long taskId) {
        Task task = taskRepository.getReferenceById(taskId);
        task.setCompleted(true);
    }
}
