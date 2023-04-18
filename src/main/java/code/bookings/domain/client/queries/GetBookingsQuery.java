package code.bookings.domain.client.queries;

import lombok.Builder;

@Builder
public record GetBookingsQuery(String username) {
}
