package com.example.ticket.domain;

import com.example.screening.domain.ScreeningFacade;
import com.example.ticket.domain.dto.ReserveTicketDTO;
import com.example.ticket.domain.dto.TicketDTO;
import com.example.ticket.domain.exception.TicketNotFoundException;
import lombok.AllArgsConstructor;
import org.springframework.transaction.annotation.Transactional;

import java.time.LocalDateTime;
import java.util.List;

@AllArgsConstructor
public class TicketAPI {

    private final TicketRepository ticketRepository;
    private final TicketFactory ticketFactory;
    private final ScreeningFacade screeningFacade;

    @Transactional
    public TicketDTO reserveTicket(ReserveTicketDTO dto) {
        screeningFacade.checkReservationPossibility(dto.screeningId(), dto.age());
        var ticket = ticketFactory.createTicket(dto);
        var addedTicket = ticketRepository.save(ticket);
        screeningFacade.decreaseFreeSeatsByOne(dto.screeningId());
        return addedTicket.toDTO();
    }

    @Transactional
    public void cancel(Long ticketId, LocalDateTime currentDate) {
        var ticket = getTicketOrThrowException(ticketId);
        screeningFacade.checkCancelReservationPossibility(ticket.getScreeningId(), currentDate);
        ticket.cancel();
    }

    public TicketDTO readTicketById(Long ticketId) {
        return getTicketOrThrowException(ticketId).toDTO();
    }

    public List<TicketDTO> readAllTickets() {
        return ticketRepository
                .findAll()
                .stream()
                .map(Ticket::toDTO)
                .toList();
    }

    private Ticket getTicketOrThrowException(Long ticketId) {
        return ticketRepository
                .findById(ticketId)
                .orElseThrow(() -> new TicketNotFoundException(ticketId));
    }
}
