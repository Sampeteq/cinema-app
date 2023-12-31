package com.cinema.screenings.application;

import com.cinema.screenings.domain.ScreeningSeat;
import com.cinema.screenings.domain.ScreeningSeatRepository;
import com.cinema.tickets.domain.TicketBooked;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Component;
import org.springframework.transaction.event.TransactionPhase;
import org.springframework.transaction.event.TransactionalEventListener;

@Component
@RequiredArgsConstructor
@Slf4j
class TicketBookedHandler {

    private final ScreeningSeatRepository screeningSeatRepository;

    @TransactionalEventListener(phase = TransactionPhase.BEFORE_COMMIT)
    public void handle(TicketBooked event) {
        log.info("Handling event:{}", event);
        screeningSeatRepository
                .getById(event.seatId())
                .ifPresent(ScreeningSeat::markAsNotFree);
    }
}
