package code.bookings.domain.client.commands;

import lombok.Builder;

import java.util.UUID;

@Builder
public record CancelBookingCommand(UUID bookingId) {
}
