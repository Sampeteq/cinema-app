package code.films.infrastructure.db;

import code.films.domain.Film;

import java.util.List;
import java.util.Optional;

public interface FilmRepository  {
    Film add(Film film);

    List<Film> addMany(List<Film> films);

    Optional<Film> readById(Long filmId);

    List<Film> readAll();
}