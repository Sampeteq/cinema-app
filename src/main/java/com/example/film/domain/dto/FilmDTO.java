package com.example.film.domain.dto;

import com.example.film.domain.FilmCategory;

import java.util.UUID;

public record FilmDTO(UUID filmId, String title, FilmCategory category, int year) {
}
