package br.com.pitarelo.api_todo.domain.dto.task;

import jakarta.validation.constraints.NotNull;

public record TaskUpdate(
        String title,
        String description,
        @NotNull
        Boolean completed
) {
}
