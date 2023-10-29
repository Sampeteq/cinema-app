package com.cinema.screenings.application;

import com.cinema.screenings.domain.ScreeningRepository;
import com.cinema.screenings.domain.Seat;
import com.cinema.screenings.domain.exceptions.ScreeningNotFoundException;
import com.cinema.screenings.domain.exceptions.SeatNotFoundException;
import com.cinema.tickets.domain.events.TicketBookedEvent;
import com.cinema.tickets.domain.events.TicketCancelledEvent;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Service;
import org.springframework.transaction.event.TransactionPhase;
import org.springframework.transaction.event.TransactionalEventListener;

@Service
@RequiredArgsConstructor
@Slf4j
class TicketEventHandler {

    private final ScreeningRepository screeningRepository;

    @TransactionalEventListener(phase = TransactionPhase.BEFORE_COMMIT)
    public void handle(TicketBookedEvent event) {
        log.info("Handled event:{}", event);
        var seat = readSeat(
                event.screeningId(),
                event.seatId()
        );
        seat.take();
        log.info("Taken seat:{}", seat);
    }

    @TransactionalEventListener(phase = TransactionPhase.BEFORE_COMMIT)
    public void handle(TicketCancelledEvent event) {
        log.info("Handled event:{}", event);
        var seat = readSeat(
                event.screeningId(),
                event.seatId()
        );
        seat.free();
        log.info("Freed seat:{}", seat);
    }

    private Seat readSeat(Long screeningId, Long seatId) {
        return screeningRepository
                .readById(screeningId)
                .orElseThrow(ScreeningNotFoundException::new)
                .findSeat(seatId)
                .orElseThrow(SeatNotFoundException::new);
    }
}
