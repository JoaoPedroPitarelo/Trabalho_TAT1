package br.com.pitarelo.api_todo.domain.dto.login;

import br.com.pitarelo.api_todo.domain.model.User;

public record UserResponse(
        Long id,
        String username,
        String email
) {
    public UserResponse(User user) {
        this(user.getId(), user.getUsername(), user.getEmail());
    }
}
