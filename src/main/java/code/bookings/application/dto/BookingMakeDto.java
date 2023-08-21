package code.bookings.application.dto;

import javax.validation.constraints.NotNull;
import javax.validation.constraints.Positive;

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
