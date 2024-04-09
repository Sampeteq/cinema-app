package com.cinema.tickets.domain;

import com.cinema.halls.domain.Seat;

import java.util.Optional;
import java.util.UUID;

public interface TicketRepository {

    Ticket save(Ticket ticket);

    Optional<Ticket> getById(UUID id);

    Optional<Ticket> getByScreeningIdAndSeat(UUID id, Seat seat);

    Optional<Ticket> getByIdAndUserId(UUID ticketId, UUID userId);
}