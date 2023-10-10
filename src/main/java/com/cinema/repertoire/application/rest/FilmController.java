package com.cinema.repertoire.application.rest;

import com.cinema.repertoire.application.dto.FilmCreateDto;
import com.cinema.repertoire.application.dto.FilmDto;
import com.cinema.repertoire.application.services.FilmService;
import io.swagger.v3.oas.annotations.security.SecurityRequirement;
import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;
import org.springframework.http.HttpStatus;
import org.springframework.web.bind.annotation.DeleteMapping;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.ResponseStatus;
import org.springframework.web.bind.annotation.RestController;

import java.util.List;

@RestController
@RequestMapping("/films")
@RequiredArgsConstructor
class FilmController {

    private final FilmService filmService;

    @PostMapping
    @ResponseStatus(code = HttpStatus.CREATED)
    @SecurityRequirement(name = "basic")
    void createFilm(@RequestBody @Valid FilmCreateDto dto) {
        filmService.creteFilm(dto);
    }

    @DeleteMapping("/{title}")
    @ResponseStatus(code = HttpStatus.NO_CONTENT)
    @SecurityRequirement(name = "basic")
    void deleteFilm(@PathVariable String title) {
        filmService.delete(title);
    }

    @GetMapping
    List<FilmDto> readAllFilms() {
        return filmService.readAll();
    }
}
