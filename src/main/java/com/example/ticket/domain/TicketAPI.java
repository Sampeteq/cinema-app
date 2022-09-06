package com.example.ticket.domain;

import com.example.screening.domain.ScreeningAPI;
import com.example.ticket.domain.dto.ReserveTicketDTO;
import com.example.ticket.domain.dto.TicketDTO;
import com.example.ticket.domain.exception.TicketNotFoundException;
import lombok.AllArgsConstructor;
import org.springframework.transaction.annotation.Transactional;

import java.time.LocalDateTime;
import java.util.List;
import java.util.UUID;

@AllArgsConstructor
public class TicketAPI {

    private final TicketRepository ticketRepository;
    private final TicketFactory ticketFactory;
    private final ScreeningAPI screeningAPI;

    @Transactional
    public TicketDTO reserveTicket(ReserveTicketDTO dto) {
        screeningAPI.checkReservationPossibility(dto.screeningId(), dto.age());
        var ticket = ticketFactory.createTicket(dto);
        var addedTicket = ticketRepository.save(ticket);
        screeningAPI.decreaseFreeSeatsByOne(dto.screeningId());
        return addedTicket.toDTO();
    }

    @Transactional
    public void cancel(UUID ticketId, LocalDateTime currentDate) {
        var ticket = getTicketOrThrowException(ticketId);
        screeningAPI.checkCancelReservationPossibility(ticket.getScreeningId(), currentDate);
        ticket.cancel();
    }

    public TicketDTO readTicketById(UUID ticketId) {
        return getTicketOrThrowException(ticketId).toDTO();
    }

    public List<TicketDTO> readAllTickets() {
        return ticketRepository
                .findAll()
                .stream()
                .map(Ticket::toDTO)
                .toList();
    }

    private Ticket getTicketOrThrowException(UUID ticketId) {
        return ticketRepository
                .findById(ticketId)
                .orElseThrow(() -> new TicketNotFoundException(ticketId));
    }
}
