package com.cinema.rooms.application;

import com.cinema.rooms.domain.RoomRepository;
import com.cinema.rooms.domain.exceptions.RoomNotFoundException;
import com.cinema.screenings.domain.events.ScreeningCreatedEvent;
import com.cinema.screenings.domain.events.ScreeningEndedEvent;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Service;
import org.springframework.transaction.event.TransactionPhase;
import org.springframework.transaction.event.TransactionalEventListener;

@Service
@RequiredArgsConstructor
@Slf4j
class ScreeningEventHandler {

    private final RoomRepository roomRepository;

    @TransactionalEventListener(phase = TransactionPhase.BEFORE_COMMIT)
    public void handle(ScreeningCreatedEvent event) {
        log.info("Handled event:{}", event);
        var roomOccupation = roomRepository
                .readById(event.roomId())
                .orElseThrow(RoomNotFoundException::new)
                .addOccupation(event.start(), event.end());
        log.info("Room occupation added:{}", roomOccupation.toString());
    }

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
