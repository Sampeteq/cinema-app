package com.cinema.tickets.domain;

import com.cinema.halls.domain.Seat;

import java.util.Optional;

public interface TicketRepository {

    Ticket save(Ticket ticket);

    Optional<Ticket> getById(Long id);

    Optional<Ticket> getByScreeningIdAndSeat(Long id, Seat seat);

    Optional<Ticket> getByIdAndUserId(Long ticketId, Long userId);
}