package code.bookings.dto;

import java.util.UUID;

public record BookingCancelledEvent(UUID screeningId, UUID seatId) {
}
