package code.bookings.application.dto;

import java.util.UUID;

public record BookingCancelledEvent(UUID screeningId, UUID seatId) {
}
