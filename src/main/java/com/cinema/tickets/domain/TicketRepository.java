package com.cinema.tickets.domain;

import com.cinema.halls.domain.Seat;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;

import java.util.List;
import java.util.Optional;

public interface TicketRepository extends JpaRepository<Ticket, Long> {

    @Query("""
                from Ticket ticket
                left join fetch ticket.screening screening
                left join fetch ticket.seat seat
                where seat = :seat
            """)
    Optional<Ticket> findBySeat(Seat seat);

    @Query("""
            from Ticket ticket
            left join fetch ticket.screening screening
            left join fetch ticket.seat seat
            where ticket.id = :ticketId and ticket.userId = :userId
            """)
    Optional<Ticket> findByIdAndUserId(Long ticketId, Long userId);

    @Query("""
            from Ticket ticket
            left join fetch ticket.screening screening
            left join fetch ticket.seat seat
            left join fetch screening.film film
            where ticket.userId = :userId
            """)
    List<Ticket> findAllByUserId(Long userId);

    List<Ticket> findAllByScreeningId(Long screeningId);
}