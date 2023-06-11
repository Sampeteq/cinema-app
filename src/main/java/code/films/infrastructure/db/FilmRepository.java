package code.films.infrastructure.db;

import code.films.domain.Film;
import code.films.domain.FilmCategory;

import java.util.List;
import java.util.Optional;

public interface FilmRepository  {
    Film add(Film film);
    Optional<Film> readById(Long filmId);
    List<Film> readAll();
    Optional<Film> readByTitle(String title);
    List<Film> readByCategory(FilmCategory category);
}