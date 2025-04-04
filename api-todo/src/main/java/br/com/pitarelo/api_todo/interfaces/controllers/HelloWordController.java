package br.com.pitarelo.api_todo.interfaces.controllers;

import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.ResponseBody;
import org.springframework.web.bind.annotation.RestController;

@RestController
@RequestMapping("/")
public class HelloWordController {

    @GetMapping
    @ResponseBody
    public ResponseEntity<?> helloWorldController() {
        return ResponseEntity.ok().body("<h1>Hello Word TODO API<h1/>");
    }
}
