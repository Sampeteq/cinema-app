package com.cinema.users.infrastrcuture.rest;

import com.cinema.users.application.dto.UserPasswordNewDto;
import com.cinema.users.application.dto.UserSignUpDto;
import com.cinema.users.application.services.UserFacade;
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

