package com.cinema.tickets.infrastructure.db;

import com.cinema.halls.infrastructure.db.JpaSeat;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;

import java.util.Optional;
import java.util.UUID;

public interface JpaTicketRepository extends JpaRepository<JpaTicket, UUID> {

    @Query("""
                from JpaTicket ticket
                where ticket.screening.id = :id and ticket.seat = :seat
            """)
    Optional<JpaTicket> getByScreeningIdAndSeat(UUID id, JpaSeat seat);

    @Query("""
                from JpaTicket ticket
                left join fetch ticket.screening
                where ticket.id = :ticketId and ticket.userId = :userId
            """)
    Optional<JpaTicket> getByIdAndUserId(UUID ticketId, UUID userId);
}

