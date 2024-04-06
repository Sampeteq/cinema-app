package com.cinema.tickets.infrastructure.db;

import com.cinema.tickets.domain.TicketDto;
import com.cinema.tickets.domain.TicketReadRepository;
import lombok.RequiredArgsConstructor;

import java.util.List;

@RequiredArgsConstructor
public class JpaTicketReadRepositoryAdapter implements TicketReadRepository {

    private final JpaTicketReadRepository jpaTicketReadRepository;

    @Override
    public List<TicketDto> getByScreeningId(Long id) {
        return jpaTicketReadRepository.getByScreeningId(id);
    }

    @Override
    public List<TicketDto> getByUserId(Long id) {
        return jpaTicketReadRepository.getByUserId(id);
    }
}
