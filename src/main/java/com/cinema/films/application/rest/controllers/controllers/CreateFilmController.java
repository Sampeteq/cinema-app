package com.cinema.films.application.rest.controllers.controllers;

import com.cinema.films.application.commands.CreateFilm;
import com.cinema.films.application.commands.handlers.CreateFilmHandler;
import io.swagger.v3.oas.annotations.security.SecurityRequirement;
import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import java.net.URI;

@RestController
@RequestMapping("/films")
@RequiredArgsConstructor
@Slf4j
class CreateFilmController {

    private final CreateFilmHandler createFilmHandler;

    @PostMapping
    @SecurityRequirement(name = "basic")
    ResponseEntity<Object> createFilm(@RequestBody @Valid CreateFilm command) {
        log.info("Command:{}", command);
        createFilmHandler.handle(command);
        var responseEntity = ResponseEntity.created(URI.create("/films")).build();
        log.info("Response entity{}", responseEntity);
        return responseEntity;
    }
}
