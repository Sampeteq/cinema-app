package com.cinema.screenings.application.services;

import com.cinema.screenings.domain.ScreeningRepository;
import com.cinema.screenings.domain.Seat;
import com.cinema.screenings.domain.exceptions.ScreeningNotFoundException;
import com.cinema.screenings.domain.exceptions.SeatNotFoundException;
import com.cinema.tickets.domain.events.TicketBookedEvent;
import com.cinema.tickets.domain.events.TicketCancelledEvent;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import org.springframework.transaction.event.TransactionPhase;
import org.springframework.transaction.event.TransactionalEventListener;

@Service
@RequiredArgsConstructor
class TicketEventHandlerService {

    private final ScreeningRepository screeningRepository;

    @TransactionalEventListener(phase = TransactionPhase.BEFORE_COMMIT)
    public void handle(TicketBookedEvent event) {
        readSeat(
                event.screeningId(),
                event.seatId()
        ).take();
    }

    @TransactionalEventListener(phase = TransactionPhase.BEFORE_COMMIT)
    public void handle(TicketCancelledEvent event) {
        readSeat(
                event.screeningId(),
                event.seatId()
        ).free();
    }

    private Seat readSeat(Long screeningId, Long seatId) {
        return screeningRepository
                .readById(screeningId)
                .orElseThrow(ScreeningNotFoundException::new)
                .findSeat(seatId)
                .orElseThrow(SeatNotFoundException::new);
    }
}
