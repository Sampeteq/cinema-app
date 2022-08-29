package com.example.film.domain;

import com.example.film.domain.dto.AddFilmDTO;
import com.example.film.domain.dto.FilmDTO;
import com.example.film.domain.exception.FilmNotFoundException;
import lombok.AllArgsConstructor;
import java.util.List;
import java.util.UUID;

@AllArgsConstructor
public class FilmAPI {

    private final FilmRepository filmRepository;

    public FilmDTO addFilm(AddFilmDTO cmd) {
        return filmRepository.save(new Film(cmd.title(), cmd.filmCategory() ) ).toDTO();
    }

    public FilmDTO readFilmById(UUID filmId) {
        return filmRepository
                .findById(filmId)
                .map(Film::toDTO)
                .orElseThrow(() -> new FilmNotFoundException(filmId) );
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

    public boolean isFilmPresent(UUID filmId) {
        return filmRepository.findById(filmId).isPresent();
    }
}
