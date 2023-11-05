package com.cinema.films.application.queries.handlers;

import com.cinema.films.application.queries.dto.FilmDto;
import com.cinema.films.application.queries.dto.FilmMapper;
import com.cinema.films.application.queries.ReadFilms;
import com.cinema.films.domain.FilmRepository;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Component;

import java.util.List;

@Component
@RequiredArgsConstructor
@Slf4j
public class ReadFilmsHandler {

    private final FilmRepository filmRepository;
    private final FilmMapper filmMapper;

    public List<FilmDto> handle(ReadFilms query) {
        return filmRepository
                .readAll(query)
                .stream()
                .map(filmMapper::mapToDto)
                .toList();
    }
}
