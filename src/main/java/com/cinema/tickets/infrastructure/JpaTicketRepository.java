package com.cinema.tickets.infrastructure;

import com.cinema.halls.domain.Seat;
import com.cinema.tickets.domain.Ticket;
import com.cinema.tickets.domain.TicketRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.util.Optional;

interface JpaTicketRepository extends JpaRepository<Ticket, Long> {

    Optional<Ticket> getByScreeningIdAndSeat(Long id, Seat seat);

    Optional<Ticket> getByIdAndUserId(Long ticketId, Long userId);
}

@Repository
@RequiredArgsConstructor
class JpaTicketRepositoryAdapter implements TicketRepository {

    private final JpaTicketRepository jpaTicketRepository;

    @Override
    public Ticket save(Ticket ticket) {
        return jpaTicketRepository.save(ticket);
    }

    @Override
    public Optional<Ticket> getById(Long id) {
        return jpaTicketRepository.findById(id);
    }

    @Override
    public Optional<Ticket> getByScreeningIdAndSeat(Long id, Seat seat) {
        return jpaTicketRepository.getByScreeningIdAndSeat(id, seat);
    }

    @Override
    public Optional<Ticket> getByIdAndUserId(Long ticketId, Long userId) {
        return jpaTicketRepository.getByIdAndUserId(ticketId, userId);
    }
}
