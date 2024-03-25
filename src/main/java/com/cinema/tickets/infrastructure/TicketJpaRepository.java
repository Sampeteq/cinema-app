package com.cinema.tickets.infrastructure;

import com.cinema.halls.domain.Seat;
import com.cinema.tickets.domain.Ticket;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.Optional;

interface TicketJpaRepository extends JpaRepository<Ticket, Long> {

    Optional<Ticket> findByScreeningIdAndSeat(Long id, Seat seat);

    Optional<Ticket> findByIdAndUserId(Long ticketId, Long userId);
}
