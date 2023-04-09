package code.bookings.domain.commands;

import lombok.Builder;

import java.util.UUID;

@Builder
public record BookingMakeCommand(UUID seatId, String username) {
}
