package com.cinema.tickets.ui;

import com.cinema.tickets.domain.Ticket;
import org.springframework.stereotype.Component;

@Component
class TicketMapper {

    TicketView mapToView(Ticket ticket) {
        return new TicketView(
                ticket.getId(),
                ticket.getScreening().getFilmTitle(),
                ticket.getScreening().getDate(),
                ticket.getScreening().getHallId(),
                ticket.getSeat().rowNumber(),
                ticket.getSeat().number(),
                ticket.getUserId() == null ? null : ticket.getUserId()
        );
    }
}
