package com.cinema.tickets.domain;

import com.cinema.halls.domain.Seat;

import java.util.List;
import java.util.Optional;

public interface TicketRepository {

    Ticket add(Ticket ticket);

    Optional<Ticket> getById(Long id);

    Optional<Ticket> getBySeat(Seat seat);

    Optional<Ticket> getByIdAndUserId(Long ticketId, Long userId);

    List<Ticket> getAllByUserId(Long userId);

    List<Ticket> getAllByScreeningId(Long screeningId);
}