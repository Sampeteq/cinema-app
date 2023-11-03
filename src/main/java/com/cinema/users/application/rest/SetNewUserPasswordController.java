package com.cinema.users.application.rest;


import com.cinema.users.application.commands.SetNewUserPassword;
import com.cinema.users.application.commands.handlers.SetNewUserPasswordHandler;
import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;
import org.springframework.web.bind.annotation.PatchMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

@RestController
@RequestMapping("/users")
@RequiredArgsConstructor
class SetNewUserPasswordController {

    private final SetNewUserPasswordHandler setNewUserPasswordHandler;

    @PatchMapping("/password/new")
    void setNewUserPassword(@RequestBody @Valid SetNewUserPassword command) {
        setNewUserPasswordHandler.handle(command);
    }
}
