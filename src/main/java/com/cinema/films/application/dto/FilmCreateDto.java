package com.cinema.films.application.dto;

import com.cinema.films.domain.FilmCategory;
import jakarta.validation.constraints.NotEmpty;
import jakarta.validation.constraints.NotNull;
import jakarta.validation.constraints.Positive;

public record FilmCreateDto(
        @NotEmpty String title,
        @NotNull FilmCategory category,
        @Positive int year,
        @Positive int durationInMinutes
) {
}
