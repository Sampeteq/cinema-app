package com.cinema.screenings.application.dto;

import jakarta.validation.constraints.NotNull;

import java.time.LocalDateTime;
import java.util.UUID;

public record ScreeningCreateDto(
        @NotNull LocalDateTime date,
        @NotNull UUID filmId,
        @NotNull UUID hallId
) {
}
