package code.catalog.domain.ports;

import code.catalog.domain.Film;

import java.util.List;
import java.util.Optional;

public interface FilmRepository  {
    Film add(Film film);
    Optional<Film> readById(Long filmId);
    List<Film> readAll();
}