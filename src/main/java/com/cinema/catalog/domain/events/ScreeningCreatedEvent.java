package com.cinema.catalog.domain.events;

import com.cinema.shared.events.Event;

import java.time.LocalDateTime;

public record ScreeningCreatedEvent(
        Long id,
        LocalDateTime date,
        String filmTitle,
        String roomCustomId,
        int rowsQuantity,
        int seatsQuantityInOneRow
) implements Event {
}
