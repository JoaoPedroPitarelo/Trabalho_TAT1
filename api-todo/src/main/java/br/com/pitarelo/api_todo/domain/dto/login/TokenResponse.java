package br.com.pitarelo.api_todo.domain.dto.login;

public record TokenResponse(
        String token,
        String type,
        Long id,
        String username,
        String email
) {
    public TokenResponse(String accessToken, Long id, String username, String email) {
        this(accessToken, "Baerer", id, username, email);
    }
}
