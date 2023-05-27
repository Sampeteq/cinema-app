package code.bookings.application.commands;

import lombok.Builder;

@Builder
public record BookingCancellationCommand(Long bookingId) {
}
