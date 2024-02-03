package com.cinema.films.application.dto;

import com.cinema.films.domain.FilmCategory;
import jakarta.validation.constraints.NotNull;

public record AddFilmDto(
        @NotNull
        String title,
        @NotNull
        FilmCategory category,
        @NotNull
        Integer year,
        @NotNull
        Integer durationInMinutes
) {
}
