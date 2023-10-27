package com.cinema.films.application.services;

import com.cinema.films.application.dto.FilmCreateDto;
import com.cinema.films.application.dto.FilmDto;
import com.cinema.films.application.dto.FilmMapper;
import com.cinema.films.application.dto.FilmQueryDto;
import com.cinema.films.domain.Film;
import com.cinema.films.domain.FilmRepository;
import com.cinema.films.domain.exceptions.FilmNotFoundException;
import com.cinema.films.domain.exceptions.FilmTitleNotUniqueException;
import com.cinema.films.domain.policies.FilmYearPolicy;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Service;

import java.util.List;

@Service
@RequiredArgsConstructor
@Slf4j
public class FilmService {

    private final FilmYearPolicy filmYearPolicy;
    private final FilmRepository filmRepository;
    private final FilmMapper filmMapper;

    public void creteFilm(FilmCreateDto dto) {
        log.info("DTO:{}", dto);
        filmYearPolicy.checkFilmYear(dto.year());
        if (filmRepository.existsByTitle(dto.title())) {
            throw new FilmTitleNotUniqueException();
        }
        var film = new Film(
                dto.title(),
                dto.category(),
                dto.year(),
                dto.durationInMinutes()
        );
        var addedFilm = filmRepository.add(film);
        log.info("Added film:{}", addedFilm);
    }

    public List<FilmDto> readAll(FilmQueryDto queryDto) {
        return filmRepository
                .readAll(queryDto)
                .stream()
                .map(filmMapper::mapToDto)
                .toList();
    }

    public int readFilmDurationInMinutes(Long id) {
        return filmRepository
                .readById(id)
                .orElseThrow(FilmNotFoundException::new)
                .getDurationInMinutes();
    }

    public String readFilmTitle(Long id) {
        return filmRepository
                .readById(id)
                .orElseThrow(FilmNotFoundException::new)
                .getTitle();
    }

    public void delete(Long id) {
        var film = filmRepository
                .readById(id)
                .orElseThrow(FilmNotFoundException::new);
        filmRepository.delete(film);
    }
}
