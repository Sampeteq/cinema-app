package com.cinema.screenings.infrastructure;

import java.time.LocalDateTime;

record ScreeningDto(
        Long id,
        LocalDateTime date,
        LocalDateTime endDate,
        Long filmId,
        Long hallId
) {
}
