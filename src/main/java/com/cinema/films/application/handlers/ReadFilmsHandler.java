package com.cinema.films.application.handlers;

import com.cinema.films.application.dto.FilmDto;
import com.cinema.films.application.dto.FilmMapper;
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

    public List<FilmDto> handle(ReadFilms queryDto) {
        return filmRepository
                .readAll(queryDto)
                .stream()
                .map(filmMapper::mapToDto)
                .toList();
    }
}
