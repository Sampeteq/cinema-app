package com.cinema.catalog.domain;

import java.util.List;
import java.util.Optional;

public interface FilmRepository  {
    Film add(Film film);
    Optional<Film> readById(Long filmId);
    List<Film> readAll();
    boolean existsByTitle(String title);
}