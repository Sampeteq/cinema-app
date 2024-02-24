package com.cinema.tickets;

import org.springframework.stereotype.Component;

@Component
class TicketMapper {

    TicketView mapToView(Ticket ticket, String filmTitle) {
        return new TicketView(
                ticket.getId(),
                filmTitle,
                ticket.getScreening().getDate(),
                ticket.getScreening().getHallId(),
                ticket.getSeat().rowNumber(),
                ticket.getSeat().number(),
                ticket.getUserId() == null ? null : ticket.getUserId()
        );
    }
}
