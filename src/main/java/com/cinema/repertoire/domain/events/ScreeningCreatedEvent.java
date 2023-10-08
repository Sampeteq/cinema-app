package com.cinema.repertoire.domain.events;

import java.time.LocalDateTime;

public record ScreeningCreatedEvent(
        LocalDateTime start,
        LocalDateTime end,
        String roomId
) {
}
