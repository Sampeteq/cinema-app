package com.cinema.tickets_views.application.services;

import com.cinema.tickets_views.application.dto.TicketViewDto;
import com.cinema.tickets_views.application.dto.TicketViewMapper;
import com.cinema.tickets_views.domain.ports.TicketViewRepository;
import com.cinema.shared.exceptions.EntityNotFoundException;
import com.cinema.user.application.services.UserFacade;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;

@Service
@Transactional(readOnly = true)
@RequiredArgsConstructor
public class TicketReadService {

    private final UserFacade userFacade;
    private final TicketViewMapper ticketViewMapper;
    private final TicketViewRepository ticketViewRepository;

    public TicketViewDto readById(Long id) {
        var currentUserId = userFacade.readCurrentUserId();
        return ticketViewRepository
                .readByIdAndUserId(id, currentUserId)
                .map(ticketViewMapper::mapToDto)
                .orElseThrow(() -> new EntityNotFoundException("Ticket"));
    }

    public List<TicketViewDto> readAll() {
        var currentUserId = userFacade.readCurrentUserId();
        return ticketViewRepository
                .readAllByUserId(currentUserId)
                .stream()
                .map(ticketViewMapper::mapToDto)
                .toList();
    }
}
