package com.cinema.tickets.domain;

import com.cinema.tickets.application.dto.TicketDto;

import java.util.List;
import java.util.Optional;

public interface TicketRepository {
    Ticket add(Ticket ticket);
    Optional<Ticket> getByIdAndUserId(Long ticketId, Long userId);
    List<TicketDto> getAllByUserId(Long userId);
}
