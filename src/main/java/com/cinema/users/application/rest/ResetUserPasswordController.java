package com.cinema.users.application.rest;

import com.cinema.users.application.commands.ResetUserPassword;
import com.cinema.users.application.commands.handlers.ResetUserPasswordHandler;
import lombok.RequiredArgsConstructor;
import org.springframework.web.bind.annotation.PatchMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;

@RestController
@RequestMapping("/users")
@RequiredArgsConstructor
class ResetUserPasswordController {

    private final ResetUserPasswordHandler resetUserPasswordHandler;

    @PatchMapping("/password/reset")
    void resetUserPassword(@RequestParam String mail) {
        var resetUserPassword = new ResetUserPassword(mail);
        resetUserPasswordHandler.handle(resetUserPassword);
    }
}
