package code.reservation.dto;

import code.screening.ScreeningTicketStatus;
import lombok.Builder;

import java.math.BigDecimal;
import java.util.UUID;

@Builder
public record TicketDTO(UUID ticketUuid,
                        String firstName,
                        String lastName,
                        BigDecimal prize,
                        ScreeningTicketStatus status,
                        Long screeningId) { }
