package com.cinema.tickets.domain;

import com.cinema.halls.domain.Seat;

import java.util.Optional;

public interface TicketRepository {

    Ticket add(Ticket ticket);

    Optional<Ticket> getById(Long id);

    Optional<Ticket> getBySeat(Seat seat);

    Optional<Ticket> getByIdAndUserId(Long ticketId, Long userId);
}