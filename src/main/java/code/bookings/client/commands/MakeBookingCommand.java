package code.bookings.client.commands;

import lombok.Builder;

import java.util.UUID;

@Builder
public record MakeBookingCommand(Long seatId, String username) {
}
