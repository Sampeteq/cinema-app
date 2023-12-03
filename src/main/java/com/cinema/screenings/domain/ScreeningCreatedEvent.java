package com.cinema.screenings.domain;

import com.cinema.halls.application.queries.dto.HallDto;

import java.time.LocalDateTime;

public record ScreeningCreatedEvent(
        Long screeningId,
        LocalDateTime start,
        LocalDateTime end,
        HallDto hall
) {
}
