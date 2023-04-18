package code.bookings.client.commands.events;

import lombok.Builder;

import java.util.UUID;

@Builder
public record IncreasedFreeSeatsEvent(UUID seatId) {
}
