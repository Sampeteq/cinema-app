package code.bookings.application.commands;

import lombok.Builder;

@Builder
public record CancelBookingCommand(Long bookingId) {
}
