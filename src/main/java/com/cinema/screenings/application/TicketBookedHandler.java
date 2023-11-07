package com.cinema.screenings.application;

import com.cinema.screenings.domain.ScreeningRepository;
import com.cinema.screenings.domain.exceptions.ScreeningNotFoundException;
import com.cinema.screenings.domain.exceptions.SeatNotFoundException;
import com.cinema.tickets.domain.events.TicketBookedEvent;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Component;
import org.springframework.transaction.event.TransactionPhase;
import org.springframework.transaction.event.TransactionalEventListener;

@Component
@RequiredArgsConstructor
@Slf4j
public class TicketBookedHandler {

    private final ScreeningRepository screeningRepository;

    @TransactionalEventListener(phase = TransactionPhase.BEFORE_COMMIT)
    public void handle(TicketBookedEvent event) {
        log.info("Handled event:{}", event);
        var seat = screeningRepository
                .getById(event.screeningId())
                .orElseThrow(ScreeningNotFoundException::new)
                .findSeat(event.seatId())
                .orElseThrow(SeatNotFoundException::new);
        seat.take();
        log.info("Taken seat:{}", seat);
    }
}
