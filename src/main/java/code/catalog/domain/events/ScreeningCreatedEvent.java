package code.catalog.domain.events;

import java.time.LocalDateTime;

public record ScreeningCreatedEvent(
        Long id,
        LocalDateTime date,
        int rowsQuantity,
        int seatsQuantityInOneRow
) {
}
