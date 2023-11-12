package com.cinema.users.ui.rest.controllers;

import com.cinema.users.application.commands.CreateUser;
import com.cinema.users.application.commands.handlers.CreateUserHandler;
import io.swagger.v3.oas.annotations.tags.Tag;
import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

@RestController
@RequestMapping("/users")
@Tag(name = "users")
@RequiredArgsConstructor
class CreateUserController {

    private final CreateUserHandler createUserHandler;

    @PostMapping
    ResponseEntity<Object> createUser(@RequestBody @Valid CreateUser command) {
        createUserHandler.handle(command);
        return ResponseEntity.ok().build();
    }
}
