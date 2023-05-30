package code.bookings.application.dto;

import java.time.LocalDateTime;

public record BookingScreeningDto(
        String filmTitle,
        LocalDateTime date,
        String roomNumber,
        BookingSeatDto seat
) {
}
