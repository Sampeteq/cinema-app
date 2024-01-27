package com.cinema.films.application;

import com.cinema.films.application.dto.CreateFilmDto;
import com.cinema.films.application.dto.GetFilmsDto;
import com.cinema.films.domain.Film;
import com.cinema.films.domain.FilmFactory;
import com.cinema.films.domain.FilmRepository;
import com.cinema.films.domain.exceptions.FilmNotFoundException;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Service;

import java.util.List;

@Service
@RequiredArgsConstructor
@Slf4j
public class FilmService {

    private final FilmFactory filmFactory;
    private final FilmRepository filmRepository;

    public void createFilm(CreateFilmDto dto) {
        log.info("Dto:{}", dto);
        var film = filmFactory.createFilm(
                dto.title(),
                dto.category(),
                dto.year(),
                dto.durationInMinutes()
        );
        var addedFilm = filmRepository.add(film);
        log.info("Added film:{}", addedFilm);
    }

    public void deleteFilm(Long id) {
        log.info("Film id:{}", id);
        var film = filmRepository
                .getById(id)
                .orElseThrow(FilmNotFoundException::new);
        filmRepository.delete(film);
    }

    public List<Film> getFilms(GetFilmsDto dto) {
        return filmRepository.getAll(dto);
    }
}
