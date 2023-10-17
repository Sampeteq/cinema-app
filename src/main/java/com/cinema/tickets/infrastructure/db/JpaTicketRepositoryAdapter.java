package com.cinema.tickets.infrastructure.db;

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
    public Optional<Ticket> readById(Long id) {
        return jpaTicketRepository.findById(id);
    }

    @Override
    public List<Ticket> readAllByUserId(Long userId) {
        return jpaTicketRepository.findAllByUserId(userId);
    }

    @Override
    public boolean exists(Long screeningId, Long seatId) {
        return jpaTicketRepository.existsByScreeningIdAndSeatId(
                screeningId,
                seatId
        );
    }
}

interface JpaTicketRepository extends JpaRepository<Ticket, Long> {

    List<Ticket> findAllByUserId(Long userId);

    boolean existsByScreeningIdAndSeatId(
            Long screeningId,
            Long seatId
    );
}
