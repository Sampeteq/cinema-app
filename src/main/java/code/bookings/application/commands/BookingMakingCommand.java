package code.bookings.application.commands;

import lombok.Builder;

@Builder
public record BookingMakingCommand(Long seatId) {
}
