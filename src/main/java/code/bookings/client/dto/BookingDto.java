package code.bookings.client.dto;

import lombok.With;

import java.util.UUID;

@With
public record BookingDto(
        UUID id,

        String status,

        UUID seatId
) {
}
