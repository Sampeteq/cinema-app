package com.example.film.domain;

import com.example.film.domain.dto.AddFilmDTO;
import com.example.film.domain.dto.FilmDTO;
import com.example.film.domain.exception.FilmNotFoundException;
import lombok.AllArgsConstructor;

import java.util.List;

@AllArgsConstructor
public class FilmAPI {

    private final FilmRepository filmRepository;

    public FilmDTO addFilm(AddFilmDTO cmd) {
        var film = new Film(
                cmd.title(),
                cmd.filmCategory(),
                FilmYear.of(cmd.year() )
        );
        return filmRepository
                .save(film)
                .toDTO();
    }

    public FilmDTO readFilmById(FilmId filmId) {
        return filmRepository
                .findById(filmId)
                .map(Film::toDTO)
                .orElseThrow(() -> new FilmNotFoundException(filmId));
    }

    public List<FilmDTO> readAllFilms() {
        return filmRepository
                .findAll()
                .stream()
                .map(Film::toDTO)
                .toList();
    }

    public List<FilmDTO> readFilmsByCategory(FilmCategory category) {
        return filmRepository
                .findAllByCategory(category)
                .stream()
                .map(Film::toDTO)
                .toList();
    }

    public boolean isFilmPresent(FilmId filmId) {
        return filmRepository.findById(filmId).isPresent();
    }
}
