package code.bookings.domain.queries;

import lombok.Builder;

@Builder
public record GetBookingsQuery(String username) {
}
