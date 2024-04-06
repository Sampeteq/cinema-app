package com.cinema.films.domain;

import jakarta.validation.constraints.NotEmpty;
import jakarta.validation.constraints.NotNull;
import jakarta.validation.constraints.Positive;

public record FilmCreateDto(
        @NotEmpty String title,
        @NotNull FilmCategory category,
        @NotNull @Positive Integer year,
        @NotNull @Positive Integer durationInMinutes
) {
}
