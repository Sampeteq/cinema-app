package code.bookings.domain.client.queries;

import lombok.Builder;

import java.util.UUID;

@Builder
public record GetBookingQuery(UUID bookingId, String username) {
}
