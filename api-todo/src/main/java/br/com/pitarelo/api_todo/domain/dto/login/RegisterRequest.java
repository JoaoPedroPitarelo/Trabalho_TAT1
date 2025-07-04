package br.com.pitarelo.api_todo.domain.dto.login;

import jakarta.validation.constraints.Email;
import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.Size;

public record RegisterRequest(
        @NotBlank
        @Size(min = 3, max = 30)
        String username,

        @NotBlank
        @Size(max = 50)
        @Email
        String email,

        @NotBlank
        @Size(min = 6, max = 40)
        String password
) {

}
