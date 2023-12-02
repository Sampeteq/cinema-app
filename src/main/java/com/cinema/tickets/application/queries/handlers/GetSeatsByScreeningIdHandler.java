package com.cinema.tickets.application.queries.handlers;

import com.cinema.tickets.application.queries.GetSeatsByScreeningId;
import com.cinema.tickets.application.queries.dto.SeatDto;
import com.cinema.tickets.domain.Ticket;
import com.cinema.tickets.domain.repositories.SeatRepository;
import com.cinema.tickets.domain.repositories.TicketRepository;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Component;

import java.util.List;

@Component
@RequiredArgsConstructor
@Slf4j
public class GetSeatsByScreeningIdHandler {

    private final SeatRepository seatRepository;
    private final TicketRepository ticketRepository;

    public List<SeatDto> handle(GetSeatsByScreeningId query) {
        log.info("Query:{}", query);
        var ticketsSeats = ticketRepository
                .getAllByScreeningId(query.screeningId())
                .stream()
                .map(Ticket::getSeat)
                .toList();
        return seatRepository
                .getAllByScreeningId(query.screeningId())
                .stream()
                .map(seat -> {
                    boolean isFree;
                    isFree = !ticketsSeats.contains(seat);
                    return new SeatDto(seat.getRowNumber(), seat.getNumber(), isFree);
                })
                .toList();
    }
}
