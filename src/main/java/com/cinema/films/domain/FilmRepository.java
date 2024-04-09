package com.cinema.films.domain;

import java.util.List;
import java.util.Optional;
import java.util.UUID;

public interface FilmRepository {

    Film save(Film film);

    void delete(Film film);

    Optional<Film> getById(UUID id);

    List<Film> getAll();

    List<Film> getByTitle(String title);

    List<Film> getByCategory(FilmCategory category);
}