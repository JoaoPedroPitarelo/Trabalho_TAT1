package br.com.pitarelo.api_todo.interfaces.controllers;

import br.com.pitarelo.api_todo.domain.dto.task.TaskOutput;
import br.com.pitarelo.api_todo.domain.model.Task;
import br.com.pitarelo.api_todo.domain.dto.task.TaskCreate;
import br.com.pitarelo.api_todo.domain.dto.task.TaskUpdate;
import br.com.pitarelo.api_todo.application.services.TaskService;
import br.com.pitarelo.api_todo.domain.model.User;
import jakarta.transaction.Transactional;
import jakarta.validation.Valid;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.security.core.annotation.AuthenticationPrincipal;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.util.UriComponentsBuilder;

import java.net.URI;
import java.util.List;
import java.util.Map;

@RestController
@RequestMapping("task")
public class TaskController {

    @Autowired // Injeção de dependencies
    TaskService taskService;

    // GetAll
    @GetMapping

    public ResponseEntity<Map<String, List<TaskOutput>>> getTasks(
            @AuthenticationPrincipal User user
    ) {
        List<Task> tasks = taskService.getTasks(user.getId());

        return ResponseEntity.status(HttpStatus.OK).body(
                Map.of("tasks", tasks.stream().map(TaskOutput::new).toList())
        );
    }

    // GetById
    @GetMapping("/{taskId}")
    public ResponseEntity<?> getById(
            @PathVariable Long taskId,
            @AuthenticationPrincipal User user
    ) {
        Task task = taskService.getById(taskId, user.getId());

        if (task == null) {
            return ResponseEntity.status(HttpStatus.NOT_FOUND).body(
                    Map.of("message", "task not found")
            );
        }

        return ResponseEntity.ok(Map.of("task", new TaskOutput(task)));
    }

    // Create
    @PostMapping
    public ResponseEntity<?> createTask(
            @RequestBody @Valid TaskCreate newTask,
            UriComponentsBuilder uriBuilder,
            @AuthenticationPrincipal User user) {
        Task task = new Task(newTask);
        taskService.createTask(task, user);

        URI uri = uriBuilder.path("/{id}").buildAndExpand(task.getId()).toUri();

        return ResponseEntity.created(uri).body(task);
    }

    // Update
    @PutMapping("/{taskId}")
    @Transactional
    public ResponseEntity<?> updateTask(
            @RequestBody @Valid TaskUpdate modifiedTask,
            @PathVariable Long taskId,
            @AuthenticationPrincipal User user
    ) {
        Task task = taskService.getById(taskId, user.getId());

        if (task == null) {
            return ResponseEntity.status(HttpStatus.NOT_FOUND).body("Task not found");
        }

        taskService.updateTask(task, modifiedTask);
        return ResponseEntity.ok(task);
    }

    // Delete
    @DeleteMapping("/{taskId}")
    @Transactional
    public ResponseEntity<?> deleteTask(
            @PathVariable Long taskId,
            @AuthenticationPrincipal User user
    ) {
        Task task = taskService.getById(taskId, user.getId());

        if (task == null) {
            return ResponseEntity.status(HttpStatus.NOT_FOUND).body(Map.of("message", "task not found"));
        }

        taskService.deleteTask(taskId);
        return ResponseEntity.noContent().build();
    }
}
