package com.cinema.films.infrastrcture;

import com.cinema.films.domain.Film;
import jakarta.validation.constraints.NotEmpty;
import jakarta.validation.constraints.NotNull;
import jakarta.validation.constraints.Positive;

public record FilmCreateDto(
        @NotEmpty String title,
        @NotNull Film.Category category,
        @NotNull @Positive Integer year,
        @NotNull @Positive Integer durationInMinutes
) {
}
