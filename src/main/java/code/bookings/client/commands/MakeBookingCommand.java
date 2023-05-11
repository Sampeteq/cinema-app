package code.bookings.client.commands;

import lombok.Builder;

@Builder
public record MakeBookingCommand(Long seatId, String username) {
}
