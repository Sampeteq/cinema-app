package code.screenings.dto;

import javax.validation.constraints.NotNull;
import java.util.UUID;

public record BookScreeningTicketDto(
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
