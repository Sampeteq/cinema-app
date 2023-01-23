package code.bookings.dto;

import java.util.UUID;

public record SeatBookedEvent(UUID screeningId, UUID seatId) {
}
