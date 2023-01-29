package code.bookings.application.dto;

import java.util.UUID;

public record SeatBookedEvent(UUID screeningId, UUID seatId) {
}
