package com.cinema.screenings.domain;

import jakarta.validation.constraints.NotNull;
import jakarta.validation.constraints.Positive;

import java.time.LocalDateTime;

public record ScreeningCreateDto(
        @NotNull LocalDateTime date,
        @NotNull @Positive Long filmId,
        @NotNull @Positive Long hallId
) {
}
