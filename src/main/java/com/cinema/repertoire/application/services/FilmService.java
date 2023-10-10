package com.cinema.repertoire.application.services;

import com.cinema.repertoire.application.dto.FilmCreateDto;
import com.cinema.repertoire.application.dto.FilmDto;
import com.cinema.repertoire.application.dto.FilmMapper;
import com.cinema.repertoire.domain.Film;
import com.cinema.repertoire.domain.FilmRepository;
import com.cinema.repertoire.domain.exceptions.FilmTitleNotUniqueException;
import com.cinema.repertoire.domain.exceptions.FilmYearOutOfRangeException;
import com.cinema.shared.exceptions.EntityNotFoundException;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;

import java.time.Year;
import java.util.List;

@Service
@RequiredArgsConstructor
class FilmService {

    private final FilmRepository filmRepository;
    private final FilmMapper filmMapper;

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

    List<FilmDto> readAll() {
        return filmRepository
                .readAll()
                .stream()
                .map(filmMapper::mapToDto)
                .toList();
    }

    void delete(String title) {
        var film = filmRepository
                .readByTitle(title)
                .orElseThrow(() -> new EntityNotFoundException("Film"));
        filmRepository.delete(film);
    }

    private static boolean isFilmYearOutOfRange(Integer year) {
        var currentYear = Year.now().getValue();
        return year != currentYear - 1 && year != currentYear && year != currentYear + 1;
    }
}
