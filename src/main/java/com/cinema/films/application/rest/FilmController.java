package com.cinema.films.application.rest;

import com.cinema.films.application.dto.FilmCreateDto;
import com.cinema.films.application.dto.FilmDto;
import com.cinema.films.application.dto.FilmQueryDto;
import com.cinema.films.application.services.FilmService;
import com.cinema.films.domain.FilmCategory;
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
import org.springframework.web.bind.annotation.RequestParam;
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

    @DeleteMapping("/{id}")
    @ResponseStatus(code = HttpStatus.NO_CONTENT)
    @SecurityRequirement(name = "basic")
    void deleteFilm(@PathVariable Long id) {
        filmService.delete(id);
    }

    @GetMapping
    List<FilmDto> readAllFilms(
            @RequestParam(required = false) String title,
            @RequestParam(required = false) FilmCategory category
    ) {
        var queryDto = FilmQueryDto
                .builder()
                .title(title)
                .category(category)
                .build();
        return filmService.readAll(queryDto);
    }
}