package com.cinema.tickets;

import com.cinema.halls.Seat;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;

import java.util.List;
import java.util.Optional;

interface TicketRepository extends JpaRepository<Ticket, Long> {

    @Query("""
                from Ticket ticket
                left join fetch ticket.seat seat
                where seat = :seat
            """)
    Optional<Ticket> findBySeat(Seat seat);

    @Query("""
            from Ticket ticket
            left join fetch ticket.seat seat
            where ticket.id = :ticketId and ticket.userId = :userId
            """)
    Optional<Ticket> findByIdAndUserId(Long ticketId, Long userId);

    @Query("""
            from Ticket ticket
            left join fetch ticket.seat seat
            where ticket.userId = :userId
            """)
    List<Ticket> findAllByUserId(Long userId);

    List<Ticket> findAllByScreeningId(Long screeningId);
}