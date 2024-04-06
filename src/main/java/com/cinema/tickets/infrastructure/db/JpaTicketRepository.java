package com.cinema.tickets.infrastructure.db;

import com.cinema.halls.infrastructure.db.JpaSeat;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.Optional;

public interface JpaTicketRepository extends JpaRepository<JpaTicket, Long> {

    Optional<JpaTicket> getByScreeningIdAndSeat(Long id, JpaSeat seat);

    Optional<JpaTicket> getByIdAndUserId(Long ticketId, Long userId);
}

