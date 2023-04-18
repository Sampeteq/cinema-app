package code.bookings.client.queries;

import lombok.Builder;

@Builder
public record GetBookingsQuery(String username) {
}
