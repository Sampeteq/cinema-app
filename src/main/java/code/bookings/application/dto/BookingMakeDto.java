package code.bookings.application.dto;

import jakarta.validation.constraints.NotNull;
import jakarta.validation.constraints.Positive;

public record BookingMakeDto(
        @NotNull
        Long screeningId,

        @NotNull
        @Positive
        Integer rowNumber,

        @NotNull
        @Positive
        Integer seatNumber
) {
}
