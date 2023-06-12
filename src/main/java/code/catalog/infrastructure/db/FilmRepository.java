package code.catalog.infrastructure.db;

import code.catalog.domain.Film;
import code.catalog.domain.FilmCategory;

import java.util.List;
import java.util.Optional;

public interface FilmRepository  {
    Film add(Film film);
    Optional<Film> readById(Long filmId);
    List<Film> readAll();
    Optional<Film> readByTitle(String title);
    List<Film> readByCategory(FilmCategory category);
}