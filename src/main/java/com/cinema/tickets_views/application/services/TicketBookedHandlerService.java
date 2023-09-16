package com.cinema.tickets_views.application.services;

import com.cinema.tickets.domain.TicketStatus;
import com.cinema.tickets.domain.events.TicketBookedEvent;
import com.cinema.tickets_views.domain.TicketView;
import com.cinema.tickets_views.domain.ports.TicketViewRepository;
import com.cinema.catalog.application.services.CatalogFacade;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.context.event.EventListener;
import org.springframework.stereotype.Service;

@Service
@RequiredArgsConstructor
@Slf4j
class TicketBookedHandlerService {

    private final CatalogFacade catalogFacade;
    private final TicketViewRepository ticketViewRepository;

    @EventListener
    void handle(TicketBookedEvent event) {
        var screeningDetails = catalogFacade.readScreeningDetails(event.screeningId());
        var ticketView = new TicketView(
                event.ticketId(),
                TicketStatus.ACTIVE,
                screeningDetails.filmTitle(),
                event.screeningDate(),
                screeningDetails.roomCustomId(),
                event.seatRowNumber(),
                event.seatNumber(),
                event.userId()
        );
        ticketViewRepository.add(ticketView);
        log.info("Ticket view added: {}", ticketView);
    }
}
