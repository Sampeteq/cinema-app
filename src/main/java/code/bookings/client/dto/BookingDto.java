package code.bookings.client.dto;

import lombok.With;

import java.util.UUID;

@With
public record BookingDto(
        Long id,

        String status,

        Long seatId
) {
}
