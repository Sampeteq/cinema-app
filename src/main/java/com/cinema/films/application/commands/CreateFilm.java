package com.cinema.films.application.commands;

import com.cinema.films.domain.FilmCategory;
import jakarta.validation.constraints.NotNull;

public record CreateFilm(
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
