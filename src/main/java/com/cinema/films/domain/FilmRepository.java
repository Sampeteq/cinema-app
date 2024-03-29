package com.cinema.films.domain;

import java.util.List;
import java.util.Optional;

public interface FilmRepository  {

    Film save(Film film);

    void delete(Film film);

    Optional<Film> getById(Long id);

    List<Film> getAll();

    List<Film> getByTitle(String title);

    List<Film> getByCategory(Film.Category category);
}