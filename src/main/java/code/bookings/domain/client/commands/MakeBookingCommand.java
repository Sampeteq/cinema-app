package code.bookings.domain.client.commands;

import lombok.Builder;

import java.util.UUID;

@Builder
public record MakeBookingCommand(UUID seatId, String username) {
}
