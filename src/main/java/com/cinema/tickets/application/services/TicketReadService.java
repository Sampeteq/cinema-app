package com.cinema.tickets.application.services;

import com.cinema.tickets.application.dto.TicketDto;
import com.cinema.tickets.application.dto.TicketMapper;
import com.cinema.tickets.domain.ports.TicketRepository;
import com.cinema.user.application.services.UserFacade;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;

@Service
@Transactional(readOnly = true)
@RequiredArgsConstructor
class TicketReadService {

    private final UserFacade userFacade;
    private final TicketMapper ticketMapper;
    private final TicketRepository ticketRepository;

    public List<TicketDto> readByCurrentUser() {
        var currentUserId = userFacade.readCurrentUserId();
        return ticketRepository
                .readAllByUserId(currentUserId)
                .stream()
                .map(ticketMapper::mapToDto)
                .toList();
    }
}
