package br.com.pitarelo.api_todo.domain.model;

import jakarta.validation.constraints.NotNull;

public record TaskUpdateDTO(
        String title,
        String description,
        @NotNull
        Boolean completed
) {
}
