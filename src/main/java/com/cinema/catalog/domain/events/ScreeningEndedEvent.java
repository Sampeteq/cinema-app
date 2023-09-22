package com.cinema.catalog.domain.events;


import com.cinema.shared.events.Event;

import java.time.LocalDateTime;

public record ScreeningEndedEvent(
        String roomCustomId,
        LocalDateTime start,
        LocalDateTime end
) implements Event {
}
