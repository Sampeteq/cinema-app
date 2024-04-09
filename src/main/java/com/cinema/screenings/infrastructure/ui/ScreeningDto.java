package com.cinema.screenings.infrastructure.ui;

import java.time.LocalDateTime;
import java.util.UUID;

public record ScreeningDto(
        UUID id,
        LocalDateTime date,
        LocalDateTime endDate,
        UUID filmId,
        UUID hallId
) {
}
