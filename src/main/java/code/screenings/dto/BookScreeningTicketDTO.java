package code.screenings.dto;

import lombok.Builder;

import javax.validation.constraints.NotNull;
import java.util.UUID;

@Builder
public record BookScreeningTicketDTO(
        @NotNull
        UUID screeningId,

        @NotNull
        String firstName,

        @NotNull
        String lastName
) {
}
