package com.cinema.tickets.ui;

import com.cinema.tickets.domain.Ticket;
import org.springframework.stereotype.Component;

@Component
public class TicketMapper {

    public TicketView mapToView(Ticket ticket) {
        return new TicketView(
                ticket.getId(),
                ticket.getScreening().getFilm().getTitle(),
                ticket.getScreening().getDate(),
                ticket.getScreening().getHall().getId(),
                ticket.getSeat().rowNumber(),
                ticket.getSeat().number(),
                ticket.getUser() == null ? null : ticket.getId()
        );
    }
}
