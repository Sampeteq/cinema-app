package code.bookings.application.events;

import java.util.UUID;

public record SeatBookedEvent(UUID seatId) {
}
