package com.example.film.dto;

import com.example.film.FilmCategory;

public record FilmDTO(Long id, String title, FilmCategory category, int year) {
}
