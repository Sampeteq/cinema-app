package com.cinema.tickets;

import com.cinema.halls.Seat;
import lombok.RequiredArgsConstructor;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.util.List;
import java.util.Optional;

@Repository
@RequiredArgsConstructor
class JpaTicketRepositoryAdapter implements TicketRepository {

    interface JpaTicketRepository extends JpaRepository<Ticket, Long> {
        Optional<Ticket> findBySeat(Seat seat);

        Optional<Ticket> findByIdAndUserId(Long ticketId, Long userId);

        List<Ticket> findAllByUserId(Long userId);

        List<Ticket> findAllByScreeningId(Long screeningId);
    }

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
    public Optional<Ticket> getBySeat(Seat seat) {
        return jpaTicketRepository.findBySeat(seat);
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
    public List<Ticket> getAllByScreeningId(Long screeningId) {
        return jpaTicketRepository.findAllByScreeningId(screeningId);
    }
}
