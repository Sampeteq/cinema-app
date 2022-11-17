package code.screenings.dto;

import code.screenings.ScreeningTicketStatus;
import lombok.Builder;

import java.math.BigDecimal;
import java.util.UUID;

@Builder
public record TicketDTO(
        UUID ticketId,

        UUID screeningId,

        UUID seatId,

        String firstName,

        String lastName,

        ScreeningTicketStatus status
) {
}
