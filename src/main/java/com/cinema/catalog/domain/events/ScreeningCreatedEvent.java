package com.cinema.catalog.domain.events;

import java.time.LocalDateTime;

public record ScreeningCreatedEvent(
        LocalDateTime start,
        LocalDateTime end,
        String roomId
) {
}
