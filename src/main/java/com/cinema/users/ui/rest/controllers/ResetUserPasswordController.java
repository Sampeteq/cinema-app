package com.cinema.users.ui.rest.controllers;

import com.cinema.users.application.commands.ResetUserPassword;
import com.cinema.users.application.commands.handlers.ResetUserPasswordHandler;
import io.swagger.v3.oas.annotations.tags.Tag;
import lombok.RequiredArgsConstructor;
import org.springframework.web.bind.annotation.PatchMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;

@RestController
@RequestMapping("/users")
@Tag(name = "users")
@RequiredArgsConstructor
class ResetUserPasswordController {

    private final ResetUserPasswordHandler resetUserPasswordHandler;

    @PatchMapping("/password/reset")
    void resetUserPassword(@RequestParam String mail) {
        var resetUserPassword = new ResetUserPassword(mail);
        resetUserPasswordHandler.handle(resetUserPassword);
    }
}
