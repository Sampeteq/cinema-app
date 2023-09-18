package com.cinema.tickets.application.services;

import com.cinema.catalog.application.dto.SeatDto;
import com.cinema.tickets.application.dto.TicketBookDto;
import com.cinema.tickets.application.dto.TicketDto;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Component;

import java.util.List;

@Component
@RequiredArgsConstructor
public class TicketFacade {

    private final TicketBookService ticketBookService;
    private final TicketCancelService ticketCancelService;
    private final TicketReadService ticketReadService;
    private final SeatReadService seatReadService;

    public void bookTicket(TicketBookDto dto) {
        ticketBookService.bookTicket(dto);
    }

    public void cancelTicket(Long ticketId) {
        ticketCancelService.cancelTicket(ticketId);
    }

    public List<TicketDto> readTicketsByCurrentUser() {
        return ticketReadService.readByCurrentUser();
    }

    public List<SeatDto> readSeatsByScreeningId(Long screeningId) {
        return seatReadService.readSeatsByScreeningId(screeningId);
    }
}
