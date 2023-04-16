package code.bookings.domain.commands;

import lombok.Builder;

import java.util.UUID;

@Builder
public record CancelBookingCommand(UUID bookingId) {
}
