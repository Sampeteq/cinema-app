package com.cinema.users.application.rest;

import com.cinema.users.application.dto.UserPasswordNewDto;
import com.cinema.users.application.dto.UserCreateDto;
import com.cinema.users.application.services.UserFacade;
import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;
import org.springframework.http.HttpStatus;
import org.springframework.web.bind.annotation.PatchMapping;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.ResponseStatus;
import org.springframework.web.bind.annotation.RestController;

@RestController
@RequestMapping("/users")
@RequiredArgsConstructor
class UserController {

    private final UserFacade userFacade;

    @PostMapping
    @ResponseStatus(HttpStatus.CREATED)
    void createUser(@RequestBody @Valid UserCreateDto dto) {
        userFacade.createUser(dto);
    }

    @PatchMapping("/password/reset")
    void resetUserPassword(@RequestParam String mail) {
        userFacade.resetUserPassword(mail);
    }

    @PatchMapping("/password/new")
    void setNewUserPassword(@RequestBody @Valid UserPasswordNewDto dto) {
        userFacade.setNewUserPassword(dto);
    }
}

