package com.cinema.tickets;

import com.cinema.halls.Seat;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.List;
import java.util.Optional;

interface TicketRepository extends JpaRepository<Ticket, Long> {

    Optional<Ticket> findBySeat(Seat seat);

    Optional<Ticket> findByIdAndUserId(Long ticketId, Long userId);

    List<Ticket> findAllByUserId(Long userId);

    List<Ticket> findAllByScreeningId(Long screeningId);
}