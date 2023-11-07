package com.cinema.films.application.queries.handlers;

import com.cinema.films.application.queries.dto.FilmDto;
import com.cinema.films.application.queries.dto.FilmMapper;
import com.cinema.films.application.queries.GetFilm;
import com.cinema.films.domain.FilmRepository;
import com.cinema.films.domain.exceptions.FilmNotFoundException;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Component;

@Component
@RequiredArgsConstructor
@Slf4j
public class GetFilmHandler {

    private final FilmRepository filmRepository;
    private final FilmMapper filmMapper;

    public FilmDto handle(GetFilm query) {
        log.info("Query:{}", query);
        return filmRepository
                .getById(query.id())
                .map(filmMapper::mapToDto)
                .orElseThrow(FilmNotFoundException::new);
    }
}
