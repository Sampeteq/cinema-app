package com.cinema.users.ui;

import com.cinema.users.application.UserService;
import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.PatchMapping;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;

@RestController
@RequiredArgsConstructor
class UserController {

    private final UserService userService;

    @PostMapping("/public/users")
    ResponseEntity<Object> createUser(@RequestBody @Valid UserCreateRequest request) {
        userService.createUser(request.mail(), request.password());
        return ResponseEntity.ok().build();
    }

    @PostMapping("/public/users/password/reset")
    void resetUserPassword(@RequestParam String mail) {
        userService.resetUserPassword(mail);
    }

    @PatchMapping("/public/users/password/new")
    void setNewUserPassword(@RequestBody @Valid UserNewPasswordRequest request) {
        userService.setNewUserPassword(request.newPassword(), request.passwordResetToken());
    }
}
