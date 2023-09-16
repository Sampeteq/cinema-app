package com.cinema.user.infrastrcuture.rest;

import com.cinema.user.application.dto.UserPasswordNewDto;
import com.cinema.user.application.dto.UserSignUpDto;
import com.cinema.user.application.services.UserFacade;
import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;
import org.springframework.http.HttpStatus;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.ResponseStatus;
import org.springframework.web.bind.annotation.RestController;

@RestController
@RequiredArgsConstructor
class UserController {

    private final UserFacade userFacade;

    @PostMapping("/sign-up")
    @ResponseStatus(HttpStatus.CREATED)
    void signUpUser(@RequestBody @Valid UserSignUpDto dto) {
        userFacade.signUpUser(dto);
    }

    @PostMapping("/password/reset")
    void resetUserPassword(@RequestParam String mail) {
        userFacade.resetUserPassword(mail);
    }

    @PostMapping("/password/new")
    void setNewUserPassword(@RequestBody @Valid UserPasswordNewDto dto) {
        userFacade.setNewUserPassword(dto);
    }
}

