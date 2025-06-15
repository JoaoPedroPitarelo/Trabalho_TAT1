package br.com.pitarelo.api_todo.domain.dto.login;

public record ResponseToken(
        String username,
        String accessJWT
) {
}
