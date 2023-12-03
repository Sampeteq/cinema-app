package com.cinema.tickets.application.queries.handlers;

import com.cinema.tickets.application.queries.GetTicketsSeatIdsByScreeningId;
import com.cinema.tickets.domain.Ticket;
import com.cinema.tickets.domain.TicketRepository;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Component;

import java.util.List;

@Component
@RequiredArgsConstructor
@Slf4j
public class GetTicketsSeatIdsByScreeningHandler {

    private final TicketRepository ticketRepository;

    public List<Long> handle(GetTicketsSeatIdsByScreeningId query) {
        log.info("Query:{}", query);
        return ticketRepository
                .getAllByScreeningId(query.screeningId())
                .stream()
                .map(Ticket::getSeatId)
                .toList();
    }
}
