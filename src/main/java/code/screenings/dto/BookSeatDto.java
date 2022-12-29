package code.screenings.dto;

import javax.validation.constraints.NotNull;
import java.util.UUID;

public record BookSeatDto(
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
