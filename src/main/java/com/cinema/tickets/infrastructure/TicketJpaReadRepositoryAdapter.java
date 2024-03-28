package com.cinema.tickets.infrastructure;

import com.cinema.tickets.domain.TicketDto;
import com.cinema.tickets.domain.TicketReadRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Repository;

import java.util.List;

@Repository
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
