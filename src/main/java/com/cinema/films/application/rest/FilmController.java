package com.cinema.films.application.rest;

import com.cinema.films.application.commands.CreateFilm;
import com.cinema.films.application.commands.DeleteFilm;
import com.cinema.films.application.commands.handlers.CreateFilmHandler;
import com.cinema.films.application.commands.handlers.DeleteFilmHandler;
import com.cinema.films.application.queries.ReadFilms;
import com.cinema.films.application.queries.dto.FilmDto;
import com.cinema.films.application.queries.handlers.ReadFilmsHandler;
import com.cinema.films.domain.FilmCategory;
import io.swagger.v3.oas.annotations.security.SecurityRequirement;
import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.DeleteMapping;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;

import java.net.URI;
import java.util.List;

@RestController
@RequestMapping("/films")
@RequiredArgsConstructor
@Slf4j
class FilmController {

    private final CreateFilmHandler createFilmHandler;
    private final ReadFilmsHandler readFilmsHandler;
    private final DeleteFilmHandler deleteFilmHandler;

    @PostMapping
    @SecurityRequirement(name = "basic")
    ResponseEntity<Object> createFilm(@RequestBody @Valid CreateFilm command) {
        log.info("Command:{}", command);
        createFilmHandler.handle(command);
        var responseEntity = ResponseEntity.created(URI.create("/films")).build();
        log.info("Response entity{}", responseEntity);
        return responseEntity;
    }

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

    @GetMapping
    List<FilmDto> readAllFilms(
            @RequestParam(required = false) String title,
            @RequestParam(required = false) FilmCategory category
    ) {
        var queryDto = ReadFilms
                .builder()
                .title(title)
                .category(category)
                .build();
        return readFilmsHandler.handle(queryDto);
    }
}
