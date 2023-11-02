package com.cinema.films.application.rest;

import com.cinema.films.application.queries.ReadFilms;
import com.cinema.films.application.queries.dto.FilmDto;
import com.cinema.films.application.queries.handlers.ReadFilmsHandler;
import com.cinema.films.domain.FilmCategory;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;

import java.util.List;

@RestController
@RequestMapping("/films")
@RequiredArgsConstructor
@Slf4j
class ReadFilmController {

    private final ReadFilmsHandler readFilmsHandler;

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
