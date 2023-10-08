package com.cinema.repertoire.domain;

import java.util.List;
import java.util.Optional;

public interface FilmRepository  {
    Film add(Film film);
    void delete(Film film);
    Optional<Film> readByTitle(String title);
    List<Film> readAll();
    boolean existsByTitle(String title);
}