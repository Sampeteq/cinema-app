package com.example.film;

import com.example.film.dto.AddFilmDTO;
import com.example.film.dto.FilmDTO;
import com.example.film.exception.FilmNotFoundException;
import lombok.AllArgsConstructor;

import java.util.List;

@AllArgsConstructor
public class FilmFacade {

    private final FilmRepository filmRepository;

    public FilmDTO add(AddFilmDTO cmd) {
        var film = new Film(
                cmd.title(),
                cmd.filmCategory(),
                FilmYear.of(cmd.year())
        );
        return filmRepository
                .save(film)
                .toDTO();
    }

    public FilmDTO read(Long filmId) {
        return filmRepository
                .findById(filmId)
                .map(Film::toDTO)
                .orElseThrow(() -> new FilmNotFoundException(filmId));
    }

    public List<FilmDTO> readAll() {
        return filmRepository
                .findAll()
                .stream()
                .map(Film::toDTO)
                .toList();
    }

    public List<FilmDTO> readAllByCategory(FilmCategory category) {
        return filmRepository
                .findAllByCategory(category)
                .stream()
                .map(Film::toDTO)
                .toList();
    }

    public boolean isPresent(Long filmId) {
        return filmRepository.findById(filmId).isPresent();
    }
}
