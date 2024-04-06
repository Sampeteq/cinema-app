package com.cinema.films.infrastrcture.db;

import com.cinema.films.domain.FilmCategory;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.List;

public interface JpaFilmRepository extends JpaRepository<JpaFilm, Long> {

    List<JpaFilm> getByTitle(String title);

    List<JpaFilm> getByCategory(FilmCategory category);
}

