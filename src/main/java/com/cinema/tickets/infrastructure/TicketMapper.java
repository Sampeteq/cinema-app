package com.cinema.tickets.infrastructure;

import com.cinema.tickets.domain.Ticket;
import org.springframework.stereotype.Component;

import java.time.LocalDateTime;

@Component
class TicketMapper {

    TicketView mapToView(Ticket ticket, String filmTitle, LocalDateTime date, Long hallId) {
        return new TicketView(
                ticket.getId(),
                filmTitle,
                date,
                hallId,
                ticket.getSeat().rowNumber(),
                ticket.getSeat().number(),
                ticket.getUserId() == null ? null : ticket.getUserId()
        );
    }
}
