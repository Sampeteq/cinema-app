package com.cinema.tickets.infrastructure.db;

import com.cinema.tickets.application.dto.TicketDto;
import com.cinema.tickets.application.dto.TicketUserDto;
import com.cinema.tickets.domain.TicketReadRepository;
import lombok.RequiredArgsConstructor;

import java.util.List;
import java.util.UUID;

@RequiredArgsConstructor
public class JpaTicketReadRepositoryAdapter implements TicketReadRepository {

    private final JpaTicketReadRepository jpaTicketReadRepository;

    @Override
    public List<TicketDto> getByScreeningId(UUID id) {
        return jpaTicketReadRepository.getByScreeningId(id);
    }

    @Override
    public List<TicketUserDto> getByUserId(UUID id) {
        return jpaTicketReadRepository.getByUserId(id);
    }
}
