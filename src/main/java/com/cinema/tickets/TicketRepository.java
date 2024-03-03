package com.cinema.tickets;

import com.cinema.halls.Seat;

import java.util.List;
import java.util.Optional;

interface TicketRepository {

    Ticket add(Ticket ticket);

    Optional<Ticket> getById(Long id);

    Optional<Ticket> getBySeat(Seat seat);

    Optional<Ticket> getByIdAndUserId(Long ticketId, Long userId);

    List<Ticket> getAllByUserId(Long userId);

    List<Ticket> getAllByScreeningId(Long screeningId);
}