package com.cinema.films.application.queries.handlers;

import com.cinema.films.application.queries.GetFilms;
import com.cinema.films.application.queries.dto.FilmDto;
import com.cinema.films.domain.FilmRepository;
import com.cinema.films.infrastructure.FilmMapper;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Component;

import java.util.List;

@Component
@RequiredArgsConstructor
@Slf4j
public class GetFilmsHandler {

    private final FilmRepository filmRepository;
    private final FilmMapper filmMapper;

    public List<FilmDto> handle(GetFilms query) {
        return filmRepository
                .getAll(query)
                .stream()
                .map(filmMapper::mapToDto)
                .toList();
    }
}
