package code.bookings.application.dto;

import javax.validation.constraints.NotNull;
import java.util.UUID;

public record BookDto(
        @NotNull
        UUID screeningId,

        @NotNull
        UUID seatId,

        @NotNull
        String firstName,

        @NotNull
        String lastName
) {
}
