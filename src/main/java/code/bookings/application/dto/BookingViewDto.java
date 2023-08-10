package code.bookings.application.dto;

import code.bookings.domain.BookingStatus;

import java.time.LocalDateTime;

public record BookingViewDto(
        Long id,
        BookingStatus status,
        String filmTitle,
        LocalDateTime screeningDate,
        String roomCustomId,
        Integer seatRowNumber,
        Integer seatNumber
) {
}
