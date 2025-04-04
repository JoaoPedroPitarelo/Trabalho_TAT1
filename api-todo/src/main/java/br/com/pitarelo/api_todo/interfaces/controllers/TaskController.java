package br.com.pitarelo.api_todo.interfaces.controllers;

import br.com.pitarelo.api_todo.domain.model.Task;
import br.com.pitarelo.api_todo.domain.model.TaskCreateDTO;
import br.com.pitarelo.api_todo.domain.model.TaskUpdateDTO;
import br.com.pitarelo.api_todo.application.services.TaskService;
import jakarta.transaction.Transactional;
import jakarta.validation.Valid;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.util.UriComponentsBuilder;

import java.net.URI;
import java.util.List;

@RestController
@RequestMapping("task")
public class TaskController {

    @Autowired // Injeção de dependencies
    TaskService taskService;

    // GetAll
    @GetMapping
    public ResponseEntity<List<Task>> getTasks() {
        List<Task> listTasks = taskService.getTasks();

        return ResponseEntity.status(HttpStatus.OK).body(listTasks);
    }

    // GetById
    @GetMapping("/{taskId}")
    public ResponseEntity<?> getById(@PathVariable Long taskId) {
        Task task = taskService.getById(taskId);

        if (task == null) {
            return ResponseEntity.status(HttpStatus.NOT_FOUND).body("Task not found");
        }

        return ResponseEntity.ok(task);
    }

    // Create
    @PostMapping
    public ResponseEntity<?> createTask(@RequestBody @Valid TaskCreateDTO newTask, UriComponentsBuilder uriBuilder) {
        Task task = new Task(newTask);
        taskService.createTask(task);

        URI uri = uriBuilder.path("/{id}").buildAndExpand(task.getId()).toUri();

        return ResponseEntity.created(uri).body(task);
    }

    // Update
    @PutMapping("/{taskId}")
    @Transactional
    public ResponseEntity<?> updateTask(@RequestBody @Valid TaskUpdateDTO modifiedTask, @PathVariable Long taskId) {
        Task task = taskService.getById(taskId);

        if (task == null) {
            return ResponseEntity.status(HttpStatus.NOT_FOUND).body("Task not found");
        }

        taskService.updateTask(task, modifiedTask);
        return ResponseEntity.ok(task);
    }

    // Delete
    @DeleteMapping("/{taskId}")
    @Transactional
    public ResponseEntity<?> deleteTask(@PathVariable Long taskId) {
        Task task = taskService.getById(taskId);

        if (task == null) {
            return ResponseEntity.status(HttpStatus.NOT_FOUND).body("Task not found");
        }

        taskService.deleteTask(taskId);
        return ResponseEntity.noContent().build();
    }
}
