package code.bookings.client.dto;

import lombok.With;

@With
public record BookingDto(
        Long id,

        String status,

        Long seatId
) {
}
