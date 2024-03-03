package com.cinema.films.domain;

import com.cinema.films.domain.exceptions.FilmNotFoundException;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Service;

import java.util.List;

@Service
@RequiredArgsConstructor
@Slf4j
public class FilmService {

    private final FilmRepository filmRepository;

    public Film addFilm(Film film) {
        log.info("Film:{}", film);
        var addedFilm = filmRepository.save(film);
        log.info("Added film:{}", addedFilm);
        return addedFilm;
    }

    public void deleteFilm(Long id) {
        log.info("Film id:{}", id);
        var film = filmRepository
                .getById(id)
                .orElseThrow(FilmNotFoundException::new);
        filmRepository.delete(film);
    }

    public Film getFilmById(Long filmId) {
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

    public List<Film> getFilmsByCategory(Film.Category category) {
        return filmRepository.getByCategory(category);
    }
}
