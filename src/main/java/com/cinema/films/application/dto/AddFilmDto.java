package com.cinema.films.application.dto;

import com.cinema.films.domain.Film;
import jakarta.validation.constraints.NotNull;

public record AddFilmDto(
        @NotNull
        String title,
        @NotNull
        Film.Category category,
        @NotNull
        Integer year,
        @NotNull
        Integer durationInMinutes
) {
}
