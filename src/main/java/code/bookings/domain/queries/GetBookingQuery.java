package code.bookings.domain.queries;

import lombok.Builder;

import java.util.UUID;

@Builder
public record GetBookingQuery(UUID bookingId, String username) {
}
