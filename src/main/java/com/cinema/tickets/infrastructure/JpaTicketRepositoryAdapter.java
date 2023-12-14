package com.cinema.tickets.infrastructure;

import com.cinema.tickets.domain.Ticket;
import com.cinema.tickets.domain.TicketRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.util.List;
import java.util.Optional;

@Repository
@RequiredArgsConstructor
class JpaTicketRepositoryAdapter implements TicketRepository {

    private final JpaTicketRepository jpaTicketRepository;

    @Override
    public Ticket add(Ticket ticket) {
        return jpaTicketRepository.save(ticket);
    }


    @Override
    public Optional<Ticket> getByIdAndUserId(Long ticketId, Long userId) {
        return jpaTicketRepository.findByIdAndUserId(ticketId, userId);
    }

    @Override
    public List<Ticket> getAllByUserId(Long userId) {
        return jpaTicketRepository.findAllByUserId(userId);
    }

    @Override
    public boolean existsBySeatId(Long seatId) {
        return jpaTicketRepository.existsBySeatId(seatId);
    }
}

interface JpaTicketRepository extends JpaRepository<Ticket, Long> {
    Optional<Ticket> findByIdAndUserId(Long ticketId, Long userId);
    List<Ticket> findAllByUserId(Long userId);
    boolean existsBySeatId(Long seatId);
}
