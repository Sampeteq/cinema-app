package code.screenings.dto;

import code.screenings.ScreeningTicketStatus;
import lombok.Builder;

import java.math.BigDecimal;
import java.util.UUID;

@Builder
public record TicketDTO(UUID ticketId,
                        String firstName,
                        String lastName,
                        BigDecimal prize,
                        ScreeningTicketStatus status,
                        UUID screeningId) {
}
