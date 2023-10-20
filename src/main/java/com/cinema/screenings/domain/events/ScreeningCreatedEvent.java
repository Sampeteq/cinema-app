package com.cinema.screenings.domain.events;

import java.time.LocalDateTime;

public record ScreeningCreatedEvent(
        LocalDateTime start,
        LocalDateTime end,
        String roomId
) {
}
