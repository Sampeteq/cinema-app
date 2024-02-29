package com.cinema.films;

import org.springframework.data.jpa.repository.JpaRepository;

import java.util.List;

interface FilmRepository extends JpaRepository<Film, Long> {
    List<Film> findByTitle(String title);
    List<Film> findByCategory(Film.Category category);
}