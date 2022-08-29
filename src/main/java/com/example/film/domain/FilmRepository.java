package com.example.film.domain;

import org.springframework.data.jpa.repository.JpaRepository;

import java.util.List;
import java.util.UUID;

interface FilmRepository extends JpaRepository<Film, UUID> {

    List<Film> findAllByCategory(FilmCategory category);
}
