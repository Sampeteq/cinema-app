package code.screenings.dto;

import lombok.Builder;

import java.util.UUID;

@Builder
public record ScreeningTicketDto(
        UUID ticketId,

        UUID screeningId,

        UUID seatId,

        String firstName,

        String lastName,

        ScreeningTicketStatusDto status
) {
}
