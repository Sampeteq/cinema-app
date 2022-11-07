package code.bookings.dto;

import code.bookings.ScreeningTicketStatus;
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
