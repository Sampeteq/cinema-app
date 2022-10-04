package com.example.film.domain.dto;

import com.example.film.domain.FilmCategory;

public record FilmDTO(Long id, String title, FilmCategory category, int year) {
}
