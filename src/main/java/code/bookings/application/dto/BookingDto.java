package code.bookings.application.dto;

import lombok.With;

@With
public record BookingDto(
        Long id,

        String status,

        BookingScreeningDto screening
) {
}
