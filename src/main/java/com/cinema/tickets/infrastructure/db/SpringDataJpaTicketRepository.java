package com.cinema.tickets.infrastructure.db;

import com.cinema.tickets.domain.Ticket;
import com.cinema.tickets.domain.ports.TicketRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import org.springframework.stereotype.Repository;

import java.util.Optional;

@Repository
@RequiredArgsConstructor
class SpringDataJpaTicketRepository implements TicketRepository {

    private final JpaTicketRepository jpaTicketRepository;

    @Override
    public Ticket add(Ticket ticket) {
        return jpaTicketRepository.save(ticket);
    }


    @Override
    public Optional<Ticket> readByIdAndUserId(Long ticketId, Long userId) {
        return jpaTicketRepository.readByIdAndUserId(ticketId, userId);
    }
}

interface JpaTicketRepository extends JpaRepository<Ticket, Long> {

    @Query("SELECT t FROM Ticket t " +
            "LEFT JOIN FETCH t.seat s " +
            "LEFT JOIN FETCH s.screening " +
            "WHERE t.id = :ticketId and t.userId = :userId")
    Optional<Ticket> readByIdAndUserId(@Param("ticketId") Long ticketId, @Param("userId") Long userId);
}
