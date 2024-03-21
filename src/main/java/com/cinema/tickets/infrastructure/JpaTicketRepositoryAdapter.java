package com.cinema.tickets.infrastructure;

import com.cinema.halls.domain.Seat;
import com.cinema.tickets.domain.Ticket;
import com.cinema.tickets.domain.TicketRepository;
import lombok.RequiredArgsConstructor;

import java.util.List;
import java.util.Optional;

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