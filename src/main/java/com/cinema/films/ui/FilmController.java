package com.cinema.films.ui;

import com.cinema.films.application.FilmService;
import com.cinema.films.domain.Film;
import io.swagger.v3.oas.annotations.security.SecurityRequirement;
import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.DeleteMapping;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;

import java.net.URI;
import java.util.List;

@RestController
@RequiredArgsConstructor
class FilmController {

    private final FilmService filmService;

    @PostMapping("/admin/films")
    @SecurityRequirement(name = "basic")
    ResponseEntity<Film> addFilm(@RequestBody @Valid Film film) {
        var addedFilm = filmService.addFilm(film);
        return ResponseEntity
                .created(URI.create("/films"))
                .body(addedFilm);
    }

    @DeleteMapping("/admin/films/{id}")
    @SecurityRequirement(name = "basic")
    ResponseEntity<Object> deleteFilm(@PathVariable Long id) {
        filmService.deleteFilm(id);
        return ResponseEntity.noContent().build();
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
