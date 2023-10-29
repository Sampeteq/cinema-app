package com.cinema.films.application.handlers;

import com.cinema.films.application.dto.FilmDto;
import com.cinema.films.application.dto.FilmMapper;
import com.cinema.films.application.queries.ReadFilm;
import com.cinema.films.domain.FilmRepository;
import com.cinema.films.domain.exceptions.FilmNotFoundException;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Component;

@Component
@RequiredArgsConstructor
@Slf4j
public class ReadFilmHandler {

    private final FilmRepository filmRepository;
    private final FilmMapper filmMapper;

    public FilmDto handle(ReadFilm query) {
        log.info("Query:{}", query);
        return filmRepository
                .readById(query.id())
                .map(filmMapper::mapToDto)
                .orElseThrow(FilmNotFoundException::new);
    }
}
