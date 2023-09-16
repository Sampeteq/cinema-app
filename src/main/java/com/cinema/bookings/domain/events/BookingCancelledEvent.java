package com.cinema.bookings.domain.events;

import com.cinema.shared.events.Event;

public record BookingCancelledEvent(Long bookingId) implements Event {
}
