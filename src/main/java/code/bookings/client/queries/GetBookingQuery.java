package code.bookings.client.queries;

import lombok.Builder;

@Builder
public record GetBookingQuery(Long bookingId, String username) {
}
