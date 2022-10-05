package com.example.ticket.domain.dto;

import com.example.ticket.domain.TicketStatus;
import lombok.Builder;

import java.math.BigDecimal;

@Builder
public record TicketDTO(
        Long ticketId,
        String firstName,
        String lastName,
        BigDecimal prize,
        TicketStatus status,
        Long screeningId
) {
}
