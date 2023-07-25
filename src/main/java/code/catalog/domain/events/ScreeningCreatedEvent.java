package code.catalog.domain.events;

import code.shared.events.Event;

import java.time.LocalDateTime;

public record ScreeningCreatedEvent(
        Long id,
        LocalDateTime date,
        int rowsQuantity,
        int seatsQuantityInOneRow
) implements Event {
}
