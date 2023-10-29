package com.cinema.rooms.application;

import com.cinema.rooms.domain.RoomRepository;
import com.cinema.rooms.domain.exceptions.RoomNotFoundException;
import com.cinema.screenings.domain.events.ScreeningEndedEvent;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Component;
import org.springframework.transaction.event.TransactionPhase;
import org.springframework.transaction.event.TransactionalEventListener;

@Component
@RequiredArgsConstructor
@Slf4j
public class ScreeningEndedHandler {

    private final RoomRepository roomRepository;

    @TransactionalEventListener(phase = TransactionPhase.BEFORE_COMMIT)
    public void handle(ScreeningEndedEvent event) {
        log.info("Handled event:{}", event);
        roomRepository
                .readById(event.roomId())
                .orElseThrow(RoomNotFoundException::new)
                .removeOccupation(event.screeningDate());
        log.info("Removed room occupation");
    }
}
