package code.bookings.domain.client.commands.events;

import lombok.Builder;

import java.util.UUID;

@Builder
public record DecreasedFreeSeatsEvent(UUID seatId) {
}
