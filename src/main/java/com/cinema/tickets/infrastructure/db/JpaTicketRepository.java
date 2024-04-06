package com.cinema.tickets.infrastructure.db;

import com.cinema.halls.domain.Seat;
import com.cinema.halls.infrastructure.db.JpaSeat;
import com.cinema.tickets.domain.Ticket;
import com.cinema.tickets.domain.TicketRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;
import org.springframework.transaction.annotation.Transactional;

import java.util.Optional;

interface JpaTicketRepository extends JpaRepository<JpaTicket, Long> {

    Optional<JpaTicket> getByScreeningIdAndSeat(Long id, JpaSeat seat);

    Optional<JpaTicket> getByIdAndUserId(Long ticketId, Long userId);
}

@Repository
@RequiredArgsConstructor
class JpaTicketRepositoryAdapter implements TicketRepository {

    private final JpaTicketRepository jpaTicketRepository;
    private final JpaTicketMapper mapper;

    @Override
    public Ticket save(Ticket ticket) {
        return mapper.toDomain(jpaTicketRepository.save(mapper.toJpa(ticket)));
    }

    @Override
    @Transactional(readOnly = true)
    public Optional<Ticket> getById(Long id) {
        return jpaTicketRepository
                .findById(id)
                .map(mapper::toDomain);
    }

    @Override
    @Transactional(readOnly = true)
    public Optional<Ticket> getByScreeningIdAndSeat(Long id, Seat seat) {
        return jpaTicketRepository
                .getByScreeningIdAndSeat(id, new JpaSeat(seat.rowNumber(), seat.number()))
                .map(mapper::toDomain);
    }

    @Override
    @Transactional(readOnly = true)
    public Optional<Ticket> getByIdAndUserId(Long ticketId, Long userId) {
        return jpaTicketRepository
                .getByIdAndUserId(ticketId, userId)
                .map(mapper::toDomain);
    }
}
