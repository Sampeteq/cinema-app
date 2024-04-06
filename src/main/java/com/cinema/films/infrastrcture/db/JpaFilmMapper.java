package com.cinema.films.infrastrcture.db;

import com.cinema.films.domain.Film;
import org.springframework.stereotype.Component;

@Component
public class JpaFilmMapper {

    public JpaFilm toJpa(Film film) {
        return new JpaFilm(
                film.getId(),
                film.getTitle(),
                film.getCategory(),
                film.getYear(),
                film.getDurationInMinutes()
        );
    }

    public Film toDomain(JpaFilm jpaFilm) {
        return new Film(
                jpaFilm.getId(),
                jpaFilm.getTitle(),
                jpaFilm.getCategory(),
                jpaFilm.getYear(),
                jpaFilm.getDurationInMinutes()
        );
    }
}