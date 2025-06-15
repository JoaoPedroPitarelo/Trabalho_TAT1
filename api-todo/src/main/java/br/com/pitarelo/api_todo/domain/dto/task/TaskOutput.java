package br.com.pitarelo.api_todo.domain.dto.task;

import br.com.pitarelo.api_todo.domain.model.Task;

public record TaskOutput(
    Long id,
    String title,
    String description
) {
    public TaskOutput(Task task) {
        this(task.getId(), task.getTitle(), task.getDescription());
    }
}
