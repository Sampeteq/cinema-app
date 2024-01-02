package com.cinema.films.domain;

import com.cinema.films.application.dto.GetFilmsDto;

import java.util.List;
import java.util.Optional;

public interface FilmRepository  {
    Film add(Film film);
    void delete(Film film);
    Optional<Film> getById(Long id);
    List<Film> getAll(GetFilmsDto dto);
    boolean existsByTitle(String title);
}