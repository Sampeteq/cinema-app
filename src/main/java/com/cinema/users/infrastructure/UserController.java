package com.cinema.users.infrastructure;

import com.cinema.users.domain.UserService;
import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;
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
    void createUser(@RequestBody @Valid UserCreateDto userCreateDto) {
        userService.createUser(userCreateDto.mail(), userCreateDto.password());
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
