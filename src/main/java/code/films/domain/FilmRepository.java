package code.films.domain;

import code.films.application.queries.FilmReadQuery;

import java.util.List;
import java.util.Optional;

public interface FilmRepository  {
    Film add(Film film);

    List<Film> addMany(List<Film> films);

    Optional<Film> readById(Long filmId);

    List<Film> readBy(FilmReadQuery query);

    List<Film> readAll();
}