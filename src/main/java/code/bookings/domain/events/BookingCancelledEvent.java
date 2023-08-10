package code.bookings.domain.events;

import code.shared.events.Event;

public record BookingCancelledEvent(Long bookingId) implements Event {
}
