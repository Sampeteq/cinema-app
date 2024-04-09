package com.cinema.films.domain;

import com.cinema.films.domain.exceptions.FilmNotFoundException;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;

import java.util.List;
import java.util.UUID;

@RequiredArgsConstructor
@Slf4j
public class FilmService {

    private final FilmRepository filmRepository;

    public Film createFilm(FilmCreateDto filmCreateDto) {
        log.info("Dto:{}", filmCreateDto);
        var film = new Film(
                UUID.randomUUID(),
                filmCreateDto.title(),
                filmCreateDto.category(),
                filmCreateDto.year(),
                filmCreateDto.durationInMinutes()
        );
        var addedFilm = filmRepository.save(film);
        log.info("Added film:{}", addedFilm);
        return addedFilm;
    }

    public void deleteFilm(UUID id) {
        log.info("Film id:{}", id);
        var film = filmRepository
                .getById(id)
                .orElseThrow(FilmNotFoundException::new);
        filmRepository.delete(film);
    }

    public Film getFilmById(UUID filmId) {
        return filmRepository
                .getById(filmId)
                .orElseThrow(FilmNotFoundException::new);
    }

    public List<Film> getAllFilms() {
        return filmRepository.getAll();
    }

    public List<Film> getFilmsByTitle(String title) {
        return filmRepository.getByTitle(title);
    }

    public List<Film> getFilmsByCategory(FilmCategory category) {
        return filmRepository.getByCategory(category);
    }
}
