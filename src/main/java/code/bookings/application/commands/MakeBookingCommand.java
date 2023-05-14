package code.bookings.application.commands;

import lombok.Builder;

@Builder
public record MakeBookingCommand(Long seatId) {
}
