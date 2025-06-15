package br.com.pitarelo.api_todo.interfaces.controllers;

import br.com.pitarelo.api_todo.application.services.AuthenticationService;
import br.com.pitarelo.api_todo.application.services.TokenService;
import br.com.pitarelo.api_todo.domain.dto.login.LoginRequest;
import br.com.pitarelo.api_todo.domain.dto.login.RegisterRequest;
import br.com.pitarelo.api_todo.domain.dto.login.ResponseToken;
import br.com.pitarelo.api_todo.domain.dto.login.UserResponse;
import br.com.pitarelo.api_todo.domain.model.User;
import jakarta.transaction.Transactional;
import jakarta.validation.Valid;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.security.authentication.AuthenticationManager;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.util.UriComponentsBuilder;

import java.util.Map;

@RestController
@RequestMapping("user")
public class UserController {

    @Autowired
    private AuthenticationManager manager;

    @Autowired
    private TokenService tokenService;

    @Autowired
    private AuthenticationService authenticationService;

    @Transactional
    @PostMapping("/create")
    public ResponseEntity<?> createRegister(@RequestBody @Valid RegisterRequest registerRequest, UriComponentsBuilder uriBuilder) {
        User user = new User(registerRequest);
        authenticationService.saveUser(user);

        var uri = uriBuilder.path("/login/{id}").buildAndExpand(user.getId()).toUri();
        return ResponseEntity.created(uri).body(Map.of("user", new UserResponse(user)));
    }

    @Transactional
    @PostMapping("/login")
    public ResponseEntity<?> doLogin(@RequestBody @Valid LoginRequest loginRequest) {
        var authenticationToken = new UsernamePasswordAuthenticationToken(loginRequest.username(), loginRequest.password());
        var authentication = manager.authenticate(authenticationToken);

        UserDetails user = authenticationService.loadUserByUsername(loginRequest.username());

        String accessJWT = tokenService.generateAccessToken((User) authentication.getPrincipal());

        return ResponseEntity.ok(new ResponseToken(user.getUsername(), accessJWT));
    }
}
