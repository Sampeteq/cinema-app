package com.cinema.films.domain;

import com.cinema.films.application.queries.GetFilms;

import java.util.List;
import java.util.Optional;

public interface FilmRepository  {
    Film add(Film film);
    void delete(Film film);
    Optional<Film> getById(Long id);
    List<Film> getAll(GetFilms query);
    boolean existsByTitle(String title);
}