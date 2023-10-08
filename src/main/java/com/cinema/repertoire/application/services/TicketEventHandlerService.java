package com.cinema.repertoire.application.services;

import com.cinema.repertoire.domain.ScreeningRepository;
import com.cinema.repertoire.domain.Seat;
import com.cinema.shared.exceptions.EntityNotFoundException;
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
                event.rowNumber(),
                event.seatNumber()
        ).makeNotFree();
    }

    @TransactionalEventListener(phase = TransactionPhase.BEFORE_COMMIT)
    public void handle(TicketCancelledEvent event) {
        readSeat(
                event.screeningId(),
                event.rowNumber(),
                event.seatNumber()
        ).makeFree();
    }

    private Seat readSeat(Long screeningId, int rowNumber, int seatNumber) {
        return screeningRepository
                .readById(screeningId)
                .orElseThrow(() -> new EntityNotFoundException("Screening"))
                .findSeat(rowNumber, seatNumber)
                .orElseThrow(() -> new EntityNotFoundException("Seat"));
    }
}
