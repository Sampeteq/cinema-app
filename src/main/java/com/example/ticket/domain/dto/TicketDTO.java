package com.example.ticket.domain.dto;

import com.example.ticket.domain.TicketStatus;
import lombok.Builder;

import java.math.BigDecimal;
import java.util.UUID;

@Builder
public record TicketDTO(
        UUID ticketId,
        String firstName,
        String lastName,
        BigDecimal prize,
        TicketStatus status,
        UUID screeningId
) {
}
