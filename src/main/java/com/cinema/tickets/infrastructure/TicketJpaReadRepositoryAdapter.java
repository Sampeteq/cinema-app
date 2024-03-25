package com.cinema.tickets.infrastructure;

import com.cinema.tickets.domain.TicketDto;
import com.cinema.tickets.domain.TicketReadRepository;
import lombok.RequiredArgsConstructor;

import java.util.List;

@RequiredArgsConstructor
class TicketJpaReadRepositoryAdapter implements TicketReadRepository {

    private final TicketJpaReadRepository ticketJpaReadRepository;

    @Override
    public List<TicketDto> getByScreeningId(Long id) {
        return ticketJpaReadRepository.getByScreeningId(id);
    }

    @Override
    public List<TicketDto> getByUserId(Long id) {
        return ticketJpaReadRepository.getByUserId(id);
    }
}
