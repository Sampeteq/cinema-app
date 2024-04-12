package com.cinema.films.infrastrcture.ui;

import com.cinema.films.domain.Film;
import com.cinema.films.domain.FilmCategory;
import com.cinema.films.application.dto.FilmCreateDto;
import com.cinema.films.application.FilmService;
import io.swagger.v3.oas.annotations.security.SecurityRequirement;
import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;
import org.springframework.http.HttpStatus;
import org.springframework.web.bind.annotation.DeleteMapping;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.ResponseStatus;
import org.springframework.web.bind.annotation.RestController;

import java.util.List;
import java.util.UUID;

@RestController
@RequiredArgsConstructor
class FilmController {

    private final FilmService filmService;

    @PostMapping("/admin/films")
    @ResponseStatus(HttpStatus.CREATED)
    @SecurityRequirement(name = "basic")
    Film createFilm(@RequestBody @Valid FilmCreateDto filmCreateDto) {
        return filmService.createFilm(filmCreateDto);
    }

    @DeleteMapping("/admin/films/{id}")
    @ResponseStatus(HttpStatus.NO_CONTENT)
    @SecurityRequirement(name = "basic")
    void deleteFilm(@PathVariable UUID id) {
        filmService.deleteFilm(id);
    }

    @GetMapping("/public/films")
    List<Film> getAllFilms() {
        return filmService.getAllFilms();
    }

    @GetMapping("/public/films/title/{title}")
    List<Film> getAllFilms(@PathVariable String title) {
        return filmService.getFilmsByTitle(title);
    }

    @GetMapping("/public/films/category/{category}")
    List<Film> getAllFilms(@PathVariable FilmCategory category) {
        return filmService.getFilmsByCategory(category);
    }
}
