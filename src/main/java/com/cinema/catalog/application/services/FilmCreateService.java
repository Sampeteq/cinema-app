package com.cinema.catalog.application.services;

import com.cinema.catalog.application.dto.FilmCreateDto;
import com.cinema.catalog.domain.Film;
import com.cinema.catalog.domain.FilmRepository;
import com.cinema.catalog.domain.exceptions.FilmTitleNotUniqueException;
import com.cinema.catalog.domain.exceptions.FilmYearOutOfRangeException;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;

import java.time.Year;

@Service
@RequiredArgsConstructor
class FilmCreateService {

    private final FilmRepository filmRepository;

    void creteFilm(FilmCreateDto dto) {
        if (isFilmYearOutOfRange(dto.year())) {
            throw new FilmYearOutOfRangeException();
        }
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

    private static boolean isFilmYearOutOfRange(Integer year) {
        var currentYear = Year.now().getValue();
        return year != currentYear - 1 && year != currentYear && year != currentYear + 1;
    }
}
