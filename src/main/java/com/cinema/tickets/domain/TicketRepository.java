package com.cinema.tickets.domain;

import java.util.List;
import java.util.Optional;

public interface TicketRepository {
    Ticket add(Ticket ticket);
    Optional<Ticket> readByIdAndUserId(Long ticketId, Long userId);
    List<Ticket> readAllByUserId(Long userId);
    boolean exists(Long screeningId, int rowNumber, int seatNumber);
}
