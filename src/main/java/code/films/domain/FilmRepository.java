package code.films.domain;

import code.films.client.queries.GetFilmsQuery;

import java.util.List;
import java.util.Optional;
import java.util.UUID;

public interface FilmRepository  {
    Film add(Film film);

    List<Film> addMany(List<Film> films);

    Optional<Film> readById(Long filmId);

    List<Film> readBy(GetFilmsQuery query);
}