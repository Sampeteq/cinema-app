package com.cinema.tickets.domain;

import com.cinema.halls.domain.Seat;

import java.util.Optional;

public interface TicketRepository {

    Ticket save(Ticket ticket);

    Optional<Ticket> getById(long id);

    Optional<Ticket> getByScreeningIdAndSeat(long id, Seat seat);

    Optional<Ticket> getByIdAndUserId(long ticketId, long userId);
}