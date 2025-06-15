package br.com.pitarelo.api_todo.domain.dto.task;

import jakarta.validation.constraints.NotNull;

public record TaskCreate(
        @NotNull
        String title,
        String description) {
}
