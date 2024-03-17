package com.cinema.tickets.infrastructure;

import com.cinema.tickets.domain.Ticket;
import org.springframework.stereotype.Component;

@Component
class TicketMapper {

    TicketView mapToView(Ticket ticket) {
        return new TicketView(
                ticket.getId(),
                ticket.getScreening().getFilm().getTitle(),
                ticket.getScreening().getDate(),
                ticket.getScreening().getHall().getId(),
                ticket.getSeat().rowNumber(),
                ticket.getSeat().number(),
                ticket.getUserId()
        );
    }
}
