package code.screenings.dto;

import lombok.Builder;

import java.util.UUID;

@Builder
public record ScreeningTicketDTO(
        UUID ticketId,

        UUID screeningId,

        UUID seatId,

        String firstName,

        String lastName,

        ScreeningTicketStatusDTO status
) {
}
