package code.bookings.client.queries;

import lombok.Builder;

import java.util.UUID;

@Builder
public record GetBookingQuery(Long bookingId, String username) {
}
