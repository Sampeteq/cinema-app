package com.cinema.films.application.handlers;

import com.cinema.films.application.queries.ReadFilmTitle;
import com.cinema.films.domain.FilmRepository;
import com.cinema.films.domain.exceptions.FilmNotFoundException;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Component;

@Component
@RequiredArgsConstructor
@Slf4j
public class ReadFilmTitleHandler {

    private final FilmRepository filmRepository;

    public String handle(ReadFilmTitle query) {
        log.info("Query:{}", query);
        return filmRepository
                .readById(query.filmId())
                .orElseThrow(FilmNotFoundException::new)
                .getTitle();
    }
}
