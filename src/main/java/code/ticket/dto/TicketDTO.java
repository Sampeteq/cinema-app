package code.ticket.dto;

import code.ticket.TicketStatus;
import lombok.Builder;

import java.math.BigDecimal;
import java.util.UUID;

@Builder
public record TicketDTO(
        Long ticketId,
        UUID ticketUuid,
        String firstName,
        String lastName,
        BigDecimal prize,
        TicketStatus status,
        Long screeningId
) {
}
