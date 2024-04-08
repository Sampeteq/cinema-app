package com.cinema.screenings.infrastructure.ui;

import java.time.LocalDateTime;

public record ScreeningDto(
        long id,
        LocalDateTime date,
        LocalDateTime endDate,
        long filmId,
        long hallId
) {
}
