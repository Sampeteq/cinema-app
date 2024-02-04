package com.cinema.tickets.infrastructure;

import com.cinema.tickets.domain.Ticket;
import com.cinema.tickets.ui.TicketView;
import org.springframework.stereotype.Component;

@Component
public class TicketMapper {

    public TicketView mapToView(Ticket ticket) {
        return new TicketView(
                ticket.getId(),
                ticket.getStatus(),
                ticket.getScreening().getFilm().getTitle(),
                ticket.getScreening().getDate(),
                ticket.getScreening().getHall().getId(),
                ticket.getSeat().getRowNumber(),
                ticket.getSeat().getNumber()
        );
    }
}
