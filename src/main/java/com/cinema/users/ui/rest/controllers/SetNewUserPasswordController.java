package com.cinema.users.ui.rest.controllers;


import com.cinema.users.application.commands.SetNewUserPassword;
import com.cinema.users.application.commands.handlers.SetNewUserPasswordHandler;
import io.swagger.v3.oas.annotations.tags.Tag;
import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;
import org.springframework.web.bind.annotation.PatchMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

@RestController
@RequestMapping("/users")
@Tag(name = "users")
@RequiredArgsConstructor
class SetNewUserPasswordController {

    private final SetNewUserPasswordHandler setNewUserPasswordHandler;

    @PatchMapping("/password/new")
    void setNewUserPassword(@RequestBody @Valid SetNewUserPassword command) {
        setNewUserPasswordHandler.handle(command);
    }
}
