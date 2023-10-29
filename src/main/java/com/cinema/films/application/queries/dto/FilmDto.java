package com.cinema.films.application.queries.dto;


import com.cinema.films.domain.FilmCategory;

public record FilmDto(
        String title,
        FilmCategory category,
        int year,
        int durationInMinutes
) {
}
