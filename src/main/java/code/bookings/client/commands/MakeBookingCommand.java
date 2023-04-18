package code.bookings.client.commands;

import lombok.Builder;

import java.util.UUID;

@Builder
public record MakeBookingCommand(UUID seatId, String username) {
}
