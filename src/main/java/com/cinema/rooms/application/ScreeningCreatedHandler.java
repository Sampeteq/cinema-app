package com.cinema.rooms.application;

import com.cinema.rooms.domain.RoomRepository;
import com.cinema.rooms.domain.exceptions.RoomNotFoundException;
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

    private final RoomRepository roomRepository;

    @TransactionalEventListener(phase = TransactionPhase.BEFORE_COMMIT)
    public void handle(ScreeningCreatedEvent event) {
        log.info("Handled event:{}", event);
        var roomOccupation = roomRepository
                .getById(event.room().id())
                .orElseThrow(RoomNotFoundException::new)
                .addOccupation(event.start(), event.end());
        log.info("Room occupation added:{}", roomOccupation.toString());
    }
}
