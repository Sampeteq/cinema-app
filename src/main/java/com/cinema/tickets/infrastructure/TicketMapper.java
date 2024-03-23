package com.cinema.tickets.infrastructure;

import com.cinema.tickets.domain.Ticket;
import org.springframework.stereotype.Component;

@Component
class TicketMapper {

    TicketDto toDto(Ticket ticket) {
        return new TicketDto(
                ticket.getId(),
                ticket.getScreening().getFilm().getTitle(),
                ticket.getScreening().getDate(),
                ticket.getScreening().getHall().getId(),
                ticket.getSeat().rowNumber(),
                ticket.getSeat().number(),
                ticket.getUser() == null ? null : ticket.getUser().getId()
        );
    }
}
