package com.cinema.catalog.application.services;

import com.cinema.catalog.application.dto.FilmCreateDto;
import com.cinema.catalog.domain.Film;
import com.cinema.catalog.domain.exceptions.FilmTitleNotUniqueException;
import com.cinema.catalog.domain.FilmRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;

@Service
@RequiredArgsConstructor
class FilmCreateService {

    private final FilmRepository filmRepository;

    void creteFilm(FilmCreateDto dto) {
        if (filmRepository.existsByTitle(dto.title())) {
            throw new FilmTitleNotUniqueException();
        }
        var film = Film.create(
                        dto.title(),
                        dto.category(),
                        dto.year(),
                        dto.durationInMinutes()
        );
        filmRepository.add(film);
    }
}
