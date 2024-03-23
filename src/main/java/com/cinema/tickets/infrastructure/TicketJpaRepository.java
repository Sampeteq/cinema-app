package com.cinema.tickets.infrastructure;

import com.cinema.halls.domain.Seat;
import com.cinema.tickets.domain.Ticket;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.List;
import java.util.Optional;

interface TicketJpaRepository extends JpaRepository<Ticket, Long> {
    Optional<Ticket> findBySeat(Seat seat);

    Optional<Ticket> findByIdAndUserId(Long ticketId, Long userId);

    List<Ticket> findAllByUserId(Long userId);

    List<Ticket> findAllByScreeningId(Long screeningId);
}