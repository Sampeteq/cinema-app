package com.cinema.films.domain;

import com.cinema.films.application.dto.FilmQueryDto;

import java.util.List;
import java.util.Optional;

public interface FilmRepository  {
    Film add(Film film);
    void delete(Film film);
    Optional<Film> readById(Long id);
    List<Film> readAll(FilmQueryDto queryDto);
    boolean existsByTitle(String title);
}