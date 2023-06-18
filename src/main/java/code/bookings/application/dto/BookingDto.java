package code.bookings.application.dto;

import code.bookings.domain.BookingStatus;
import lombok.With;

import java.time.LocalDateTime;

@With
public record BookingDto(
        Long id,
        BookingStatus status,
        String film,
        LocalDateTime screeningDate,
        String room,
        int row,
        int seat
) {
}
