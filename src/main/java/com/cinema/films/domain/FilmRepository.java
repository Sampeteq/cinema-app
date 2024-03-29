package com.cinema.films.domain;

import org.springframework.data.jpa.repository.JpaRepository;

import java.util.List;

public interface FilmRepository extends JpaRepository<Film, Long> {

    List<Film> getByTitle(String title);

    List<Film> getByCategory(Film.Category category);
}