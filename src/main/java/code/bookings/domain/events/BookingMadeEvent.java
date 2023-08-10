package code.bookings.domain.events;

import code.shared.events.Event;

import java.time.LocalDateTime;

public record BookingMadeEvent(
        Long bookingId,
        Long screeningId,
        LocalDateTime screeningDate,
        Integer seatRowNumber,
        Integer seatNumber,
        Long userId
) implements Event {
}
