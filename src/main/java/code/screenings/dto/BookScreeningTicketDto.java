package code.screenings.dto;

import lombok.Builder;

import javax.validation.constraints.NotNull;
import java.util.UUID;

@Builder
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
