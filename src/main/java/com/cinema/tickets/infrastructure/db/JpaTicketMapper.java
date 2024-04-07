package com.cinema.tickets.infrastructure.db;

import com.cinema.halls.domain.Seat;
import com.cinema.halls.infrastructure.db.JpaSeat;
import com.cinema.screenings.infrastructure.db.JpaScreeningMapper;
import com.cinema.tickets.domain.Ticket;
import lombok.RequiredArgsConstructor;

@RequiredArgsConstructor
public class JpaTicketMapper {

    private final JpaScreeningMapper screeningMapper;

    public JpaTicket toJpa(Ticket ticket) {
        return new JpaTicket(
                ticket.getId(),
                screeningMapper.toJpa(ticket.getScreening()),
                new JpaSeat(ticket.getSeat().rowNumber(), ticket.getSeat().number()),
                ticket.getUserId(),
                ticket.getVersion()
        );
    }

    public Ticket toDomain(JpaTicket jpaTicket) {
        return new Ticket(
                jpaTicket.getId(),
                screeningMapper.toDomain(jpaTicket.getScreening()),
                new Seat(jpaTicket.getSeat().rowNumber(), jpaTicket.getSeat().number()),
                jpaTicket.getUserId(),
                jpaTicket.getVersion()
        );
    }
}
