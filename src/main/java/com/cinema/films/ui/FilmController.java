package com.cinema.films.ui;

import com.cinema.films.application.FilmService;
import com.cinema.films.domain.Film;
import io.swagger.v3.oas.annotations.security.SecurityRequirement;
import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;
import org.springframework.http.HttpStatus;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequiredArgsConstructor
class FilmController {

    private final FilmService filmService;

    @PostMapping("/admin/films")
    @ResponseStatus(HttpStatus.CREATED)
    @SecurityRequirement(name = "basic")
    Film addFilm(@RequestBody @Valid Film film) {
        return filmService.addFilm(film);
    }

    @DeleteMapping("/admin/films/{id}")
    @ResponseStatus(HttpStatus.NO_CONTENT)
    @SecurityRequirement(name = "basic")
    void deleteFilm(@PathVariable Long id) {
        filmService.deleteFilm(id);
    }

    @GetMapping("/public/films/{title}")
    Film getFilmByTitle(@PathVariable String title) {
        return filmService.getFilmByTitle(title);
    }

    @GetMapping("/public/films")
    List<Film> getAllFilms(@RequestParam(required = false) Film.Category category) {
        return category == null ? filmService.getAllFilms() : filmService.getFilmsByCategory(category);
    }
}
