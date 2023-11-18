package com.cinema.films.application;

import com.cinema.films.application.queries.GetFilm;
import com.cinema.films.application.queries.dto.FilmDto;
import com.cinema.films.application.queries.handlers.GetFilmHandler;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Component;

@Component
@RequiredArgsConstructor
public class FilmApi {

    private final GetFilmHandler getFilmHandler;

    public FilmDto getFilmById(Long filmId) {
        return getFilmHandler.handle(new GetFilm(filmId));
    }
}
