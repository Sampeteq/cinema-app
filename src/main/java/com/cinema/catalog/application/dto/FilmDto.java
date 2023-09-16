package com.cinema.catalog.application.dto;


import com.cinema.catalog.domain.FilmCategory;

public record FilmDto(
        Long id,
        String title,
        FilmCategory category,
        int year,
        int durationInMinutes
) {
}
