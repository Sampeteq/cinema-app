package com.cinema.tickets.infrastructure.db;

import com.cinema.halls.infrastructure.db.JpaSeat;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;

import java.util.Optional;

public interface JpaTicketRepository extends JpaRepository<JpaTicket, Long> {

    @Query("""
                from JpaTicket ticket
                where ticket.screening.id = :id and ticket.seat = :seat
            """)
    Optional<JpaTicket> getByScreeningIdAndSeat(long id, JpaSeat seat);

    @Query("""
                from JpaTicket ticket
                left join fetch ticket.screening
                where ticket.id = :ticketId and ticket.userId = :userId
            """)
    Optional<JpaTicket> getByIdAndUserId(long ticketId, long userId);
}

