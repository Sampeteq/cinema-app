package com.cinema.films.ui;

import com.cinema.films.application.FilmService;
import com.cinema.films.application.dto.CreateFilmDto;
import com.cinema.films.application.dto.GetFilmsDto;
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
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;

import java.net.URI;

@RestController
@RequiredArgsConstructor
@Slf4j
public class FilmController {

    private final FilmService filmService;

    @PostMapping("/admin/films")
    @SecurityRequirement(name = "basic")
    ResponseEntity<Object> createFilm(@RequestBody @Valid CreateFilmDto dto) {
        log.info("Dto:{}", dto);
        filmService.createFilm(dto);
        var responseEntity = ResponseEntity.created(URI.create("/films")).build();
        log.info("Response entity{}", responseEntity);
        return responseEntity;
    }

    @DeleteMapping("/admin/films/{id}")
    @SecurityRequirement(name = "basic")
    ResponseEntity<Object> deleteFilm(@PathVariable Long id) {
        log.info("Film id:{}", id);
        filmService.deleteFilm(id);
        var responseEntity = ResponseEntity.noContent().build();
        log.info("Response entity:{}", responseEntity);
        return responseEntity;
    }

    @GetMapping("/public/films")
    FilmsResponse getFilms(
            @RequestParam(required = false) String title,
            @RequestParam(required = false) FilmCategory category
    ) {
        var getFilmsDto = GetFilmsDto
                .builder()
                .title(title)
                .category(category)
                .build();
        var films = filmService.getFilms(getFilmsDto);
        return new FilmsResponse(films);
    }
}
