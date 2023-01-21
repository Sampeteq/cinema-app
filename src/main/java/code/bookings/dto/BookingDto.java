package code.bookings.dto;

import java.util.UUID;

public record BookingDto(
        UUID id,

        String firstName,

        String lastName,

        SeatDto seat
) {
}
