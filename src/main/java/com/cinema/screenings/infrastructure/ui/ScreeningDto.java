package com.cinema.screenings.infrastructure.ui;

import java.time.LocalDateTime;

public record ScreeningDto(
        Long id,
        LocalDateTime date,
        LocalDateTime endDate,
        Long filmId,
        Long hallId
) {
}
