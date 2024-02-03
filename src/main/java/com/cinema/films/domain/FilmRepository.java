package com.cinema.films.domain;

import java.util.List;
import java.util.Optional;

public interface FilmRepository  {
    Film add(Film film);
    void delete(Film film);
    Optional<Film> getById(Long id);
    Optional<Film> getByTitle(String title);
    List<Film> getAll();
    List<Film> getByCategory(FilmCategory category);
}