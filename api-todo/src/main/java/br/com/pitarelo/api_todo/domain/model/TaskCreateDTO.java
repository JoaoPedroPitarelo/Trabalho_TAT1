package br.com.pitarelo.api_todo.domain.model;

import jakarta.validation.constraints.NotNull;

public record TaskCreateDTO(
        @NotNull
        String title,
        String description) {
}
