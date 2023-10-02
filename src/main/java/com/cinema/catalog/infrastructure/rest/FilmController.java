package com.cinema.catalog.infrastructure.rest;

import com.cinema.catalog.application.dto.FilmCreateDto;
import com.cinema.catalog.application.dto.FilmDto;
import com.cinema.catalog.application.services.CatalogFacade;
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

    private final CatalogFacade catalogFacade;

    @PostMapping
    @ResponseStatus(code = HttpStatus.CREATED)
    @SecurityRequirement(name = "basic")
    void createFilm(@RequestBody @Valid FilmCreateDto dto) {
        catalogFacade.createFilm(dto);
    }

    @DeleteMapping("/{title}")
    @ResponseStatus(code = HttpStatus.NO_CONTENT)
    @SecurityRequirement(name = "basic")
    void deleteFilm(@PathVariable String title) {
        catalogFacade.deleteFilm(title);
    }

    @GetMapping
    List<FilmDto> readAllFilms() {
        return catalogFacade.readAllFilms();
    }
}
