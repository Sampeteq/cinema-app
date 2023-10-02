package com.cinema.catalog.application.dto;


import com.cinema.catalog.domain.FilmCategory;

public record FilmDto(
        String title,
        FilmCategory category,
        int year,
        int durationInMinutes
) {
}
