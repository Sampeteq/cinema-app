package com.cinema.tickets.infrastructure.db;

import com.cinema.halls.domain.Seat;
import com.cinema.halls.infrastructure.db.JpaSeat;
import com.cinema.tickets.domain.Ticket;
import com.cinema.tickets.domain.TicketRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.transaction.annotation.Transactional;

import java.util.Optional;
import java.util.UUID;

@RequiredArgsConstructor
public class JpaTicketRepositoryAdapter implements TicketRepository {

    private final JpaTicketRepository jpaTicketRepository;
    private final JpaTicketMapper mapper;

    @Override
    @Transactional
    public Ticket save(Ticket ticket) {
        return mapper.toDomain(jpaTicketRepository.save(mapper.toJpa(ticket)));
    }

    @Override
    @Transactional(readOnly = true)
    public Optional<Ticket> getById(UUID id) {
        return jpaTicketRepository
                .findById(id)
                .map(mapper::toDomain);
    }

    @Override
    @Transactional(readOnly = true)
    public Optional<Ticket> getByScreeningIdAndSeat(UUID id, Seat seat) {
        return jpaTicketRepository
                .getByScreeningIdAndSeat(id, new JpaSeat(seat.rowNumber(), seat.number()))
                .map(mapper::toDomain);
    }

    @Override
    @Transactional(readOnly = true)
    public Optional<Ticket> getByIdAndUserId(UUID ticketId, UUID userId) {
        return jpaTicketRepository
                .getByIdAndUserId(ticketId, userId)
                .map(mapper::toDomain);
    }
}
