package com.cinema.films.domain;

import org.springframework.data.jpa.repository.JpaRepository;

import java.util.List;
import java.util.Optional;

public interface FilmRepository extends JpaRepository<Film, Long> {
    Optional<Film> findByTitle(String title);
    List<Film> findByCategory(Film.Category category);
}