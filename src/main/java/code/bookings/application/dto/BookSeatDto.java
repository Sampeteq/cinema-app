package code.bookings.application.dto;

import javax.validation.constraints.NotNull;
import java.util.UUID;

public record BookSeatDto(
        @NotNull
        UUID screeningId,

        @NotNull
        UUID seatId
) {
}
