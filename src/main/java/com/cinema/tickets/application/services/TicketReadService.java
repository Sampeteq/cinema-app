package com.cinema.tickets.application.services;

import com.cinema.tickets.application.dto.TicketDto;
import com.cinema.tickets.application.dto.TicketMapper;
import com.cinema.tickets.domain.TicketRepository;
import com.cinema.users.application.services.UserService;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;

@Service
@Transactional(readOnly = true)
@RequiredArgsConstructor
class TicketReadService {

    private final UserService userService;
    private final TicketRepository ticketRepository;
    private final TicketMapper ticketMapper;

    public List<TicketDto> readByCurrentUser() {
        var currentUserId = userService.readCurrentUserId();
        return ticketRepository
                .readAllByUserId(currentUserId)
                .stream()
                .map(ticketMapper::mapToDto)
                .toList();
    }
}
