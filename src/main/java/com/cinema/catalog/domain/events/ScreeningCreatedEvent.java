package com.cinema.catalog.domain.events;

import com.cinema.shared.events.Event;

import java.time.LocalDateTime;

public record ScreeningCreatedEvent(
        LocalDateTime start,
        LocalDateTime end,
        String roomId
) implements Event {
}
