package com.cinema.repertoire.application.dto;


import com.cinema.repertoire.domain.FilmCategory;

public record FilmDto(
        String title,
        FilmCategory category,
        int year,
        int durationInMinutes
) {
}
