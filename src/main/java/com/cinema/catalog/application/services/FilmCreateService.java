package com.cinema.catalog.application.services;

import com.cinema.catalog.application.dto.FilmCreateDto;
import com.cinema.catalog.domain.Film;
import com.cinema.catalog.domain.FilmRepository;
import com.cinema.catalog.domain.exceptions.FilmTitleNotUniqueException;
import com.cinema.catalog.domain.services.FilmYearValidateService;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;

@Service
@RequiredArgsConstructor
class FilmCreateService {

    private final FilmYearValidateService filmYearValidateService;
    private final FilmRepository filmRepository;

    void creteFilm(FilmCreateDto dto) {
        filmYearValidateService.validate(dto.year());
        if (filmRepository.existsByTitle(dto.title())) {
            throw new FilmTitleNotUniqueException();
        }
        var film = new Film(
                dto.title(),
                dto.category(),
                dto.year(),
                dto.durationInMinutes()
        );
        filmRepository.add(film);
    }
}
