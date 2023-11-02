package com.cinema.films.application.rest;

import com.cinema.films.application.commands.DeleteFilm;
import com.cinema.films.application.commands.handlers.DeleteFilmHandler;
import io.swagger.v3.oas.annotations.security.SecurityRequirement;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.DeleteMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

@RestController
@RequestMapping("/films")
@RequiredArgsConstructor
@Slf4j
public class DeleteFilmController {

    private final DeleteFilmHandler deleteFilmHandler;

    @DeleteMapping("/{id}")
    @SecurityRequirement(name = "basic")
    ResponseEntity<Object> deleteFilm(@PathVariable Long id) {
        var command = new DeleteFilm(id);
        log.info("Command:{}", command);
        deleteFilmHandler.handle(command);
        var responseEntity = ResponseEntity.noContent().build();
        log.info("Response entity:{}", responseEntity);
        return responseEntity;
    }
}
