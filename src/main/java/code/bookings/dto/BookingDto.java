package code.bookings.dto;

import lombok.With;

import java.util.UUID;

@With
public record BookingDto(
        UUID id,

        String firstName,

        String lastName,

        String status,

        UUID screeningId,

        UUID seatId
) {
}
