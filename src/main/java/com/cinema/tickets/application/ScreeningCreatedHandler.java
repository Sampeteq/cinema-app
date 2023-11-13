package com.cinema.tickets.application;

import com.cinema.screenings.domain.events.ScreeningCreatedEvent;
import com.cinema.tickets.domain.factories.SeatFactory;
import com.cinema.tickets.domain.repositories.SeatRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.context.event.EventListener;
import org.springframework.stereotype.Component;

@Component("ScreeningCreatedHandler - seats creation")
@RequiredArgsConstructor
public class ScreeningCreatedHandler {

    private final SeatFactory seatFactory;
    private final SeatRepository seatRepository;

    @EventListener
    public void handle(ScreeningCreatedEvent event) {
        seatFactory.createSeats(
                event.room().rowsNumber(),
                event.room().seatsNumberInOneRow(),
                event.screeningId()
        ).forEach(seatRepository::add);
    }
}
