package com.example.film.domain;

import org.springframework.data.jpa.repository.JpaRepository;

import java.util.List;

interface FilmRepository extends JpaRepository<Film, FilmId> {

    List<Film> findAllByCategory(FilmCategory category);
}
