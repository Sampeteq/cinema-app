package com.cinema.tickets.infrastructure;

import com.cinema.halls.domain.Seat;
import com.cinema.tickets.domain.Ticket;
import com.cinema.tickets.domain.TicketRepository;
import lombok.RequiredArgsConstructor;

import java.util.List;
import java.util.Optional;

@RequiredArgsConstructor
class TicketJpaRepositoryAdapter implements TicketRepository {

    private final TicketJpaRepository ticketJpaRepository;

    @Override
    public Ticket add(Ticket ticket) {
        return ticketJpaRepository.save(ticket);
    }

    @Override
    public Optional<Ticket> getById(Long id) {
        return ticketJpaRepository.findById(id);
    }

    @Override
    public Optional<Ticket> getBySeat(Seat seat) {
        return ticketJpaRepository.findBySeat(seat);
    }

    @Override
    public Optional<Ticket> getByIdAndUserId(Long ticketId, Long userId) {
        return ticketJpaRepository.findByIdAndUserId(ticketId, userId);
    }

    @Override
    public List<Ticket> getAllByUserId(Long userId) {
        return ticketJpaRepository.findAllByUserId(userId);
    }

    @Override
    public List<Ticket> getAllByScreeningId(Long screeningId) {
        return ticketJpaRepository.findAllByScreeningId(screeningId);
    }
}
