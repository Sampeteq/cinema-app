package com.cinema.tickets.domain;

import com.cinema.halls.domain.Seat;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.Optional;

public interface TicketRepository extends JpaRepository<Ticket, Long> {

    Optional<Ticket> getByScreeningIdAndSeat(Long id, Seat seat);

    Optional<Ticket> getByIdAndUserId(Long ticketId, Long userId);
}