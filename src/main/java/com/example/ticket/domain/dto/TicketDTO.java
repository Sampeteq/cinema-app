package com.example.ticket.domain.dto;

import com.example.ticket.domain.Money;
import com.example.ticket.domain.TicketStatus;
import lombok.Builder;

@Builder
public record TicketDTO(
        Long ticketId,
        String firstName,
        String lastName,
        Money prize,
        TicketStatus status,
        Long screeningId
) {
}
