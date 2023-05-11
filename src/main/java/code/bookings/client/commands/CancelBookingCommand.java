package code.bookings.client.commands;

import lombok.Builder;

@Builder
public record CancelBookingCommand(Long bookingId) {
}
