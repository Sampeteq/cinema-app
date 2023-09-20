package com.cinema.tickets.infrastructure.db;

import com.cinema.tickets.domain.Ticket;
import com.cinema.tickets.domain.TicketRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.repository.query.Param;
import org.springframework.stereotype.Repository;

import java.util.List;
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

    @Override
    public List<Ticket> readAllByUserId(Long userId) {
        return jpaTicketRepository.readAllByUserId(userId);
    }
}

interface JpaTicketRepository extends JpaRepository<Ticket, Long> {

    Optional<Ticket> readByIdAndUserId(@Param("ticketId") Long ticketId, @Param("userId") Long userId);

    List<Ticket> readAllByUserId(Long userId);
}
