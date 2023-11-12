package com.cinema.tickets.infrastructure.db;

import com.cinema.tickets.domain.Ticket;
import com.cinema.tickets.domain.repositories.TicketRepository;
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
    public Optional<Ticket> getById(Long id) {
        return jpaTicketRepository.findById(id);
    }

    @Override
    public List<Ticket> getAllByUserId(Long userId) {
        return jpaTicketRepository.findAllByUserId(userId);
    }

    @Override
    public boolean exists(Long seatId) {
        return jpaTicketRepository.existsBySeatId(seatId);
    }
}

interface JpaTicketRepository extends JpaRepository<Ticket, Long> {
    boolean existsBySeatId(Long seatId);
    List<Ticket> findAllByUserId(Long userId);
}
