package com.cinema.halls.application;

import com.cinema.halls.domain.HallRepository;
import com.cinema.halls.domain.exceptions.HallNotFoundException;
import com.cinema.screenings.domain.events.ScreeningCreatedEvent;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Component;
import org.springframework.transaction.event.TransactionPhase;
import org.springframework.transaction.event.TransactionalEventListener;

@Component
@RequiredArgsConstructor
@Slf4j
public class ScreeningCreatedHandler {

    private final HallRepository hallRepository;

    @TransactionalEventListener(phase = TransactionPhase.BEFORE_COMMIT)
    public void handle(ScreeningCreatedEvent event) {
        log.info("Handled event:{}", event);
        var hallOccupation = hallRepository
                .getById(event.hall().id())
                .orElseThrow(HallNotFoundException::new)
                .addOccupation(event.start(), event.end(), event.screeningId());
        log.info("Hall occupation added:{}", hallOccupation.toString());
    }
}
