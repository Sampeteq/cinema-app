package com.cinema.tickets.ui;

import com.cinema.tickets.domain.Ticket;
import org.springframework.stereotype.Component;

@Component
public class TicketMapper {

    public TicketView mapToView(Ticket ticket) {
        return new TicketView(
                ticket.getId(),
                ticket.isFree(),
                ticket.getScreening().getFilm().getTitle(),
                ticket.getScreening().getDate(),
                ticket.getScreening().getHall().getId(),
                ticket.getSeat().getRowNumber(),
                ticket.getSeat().getNumber()
        );
    }
}
