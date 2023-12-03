package com.cinema.users.ui;

import com.cinema.users.application.commands.CreateUser;
import com.cinema.users.application.commands.ResetUserPassword;
import com.cinema.users.application.commands.SetNewUserPassword;
import com.cinema.users.application.commands.handlers.CreateUserHandler;
import com.cinema.users.application.commands.handlers.ResetUserPasswordHandler;
import com.cinema.users.application.commands.handlers.SetNewUserPasswordHandler;
import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.PatchMapping;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;

@RestController
@RequestMapping("/users")
@RequiredArgsConstructor
class UserController {

    private final CreateUserHandler createUserHandler;
    private final ResetUserPasswordHandler resetUserPasswordHandler;
    private final SetNewUserPasswordHandler setNewUserPasswordHandler;

    @PostMapping
    ResponseEntity<Object> createUser(@RequestBody @Valid CreateUser command) {
        createUserHandler.handle(command);
        return ResponseEntity.ok().build();
    }

    @PatchMapping("/password/reset")
    void resetUserPassword(@RequestParam String mail) {
        var resetUserPassword = new ResetUserPassword(mail);
        resetUserPasswordHandler.handle(resetUserPassword);
    }

    @PatchMapping("/password/new")
    void setNewUserPassword(@RequestBody @Valid SetNewUserPassword command) {
        setNewUserPasswordHandler.handle(command);
    }
}
