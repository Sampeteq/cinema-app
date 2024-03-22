package com.cinema.films.infrastrcture;

import com.cinema.films.domain.Film;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.List;

interface FilmJpaRepository extends JpaRepository<Film, Long> {
    List<Film> getByTitle(String title);

    List<Film> getByCategory(Film.Category category);
}
