package com.cinema.users.application.rest;

import com.cinema.users.application.commands.CreateUser;
import com.cinema.users.application.commands.handlers.CreateUserHandler;
import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

@RestController
@RequestMapping("/users")
@RequiredArgsConstructor
class CreateUserController {

    private final CreateUserHandler createUserHandler;

    @PostMapping
    ResponseEntity<Object> createUser(@RequestBody @Valid CreateUser command) {
        createUserHandler.handle(command);
        return ResponseEntity.ok().build();
    }
}
