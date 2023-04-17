package code.bookings.domain.events;

import lombok.Builder;

import java.util.UUID;

@Builder
public record IncreasedFreeSeatsEvent(UUID seatId) {
}
