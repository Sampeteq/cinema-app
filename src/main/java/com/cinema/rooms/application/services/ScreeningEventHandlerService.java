package com.cinema.rooms.application.services;

import com.cinema.catalog.domain.events.ScreeningCreatedEvent;
import com.cinema.catalog.domain.events.ScreeningEndedEvent;
import com.cinema.rooms.domain.RoomRepository;
import com.cinema.shared.exceptions.EntityNotFoundException;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import org.springframework.transaction.event.TransactionPhase;
import org.springframework.transaction.event.TransactionalEventListener;

@Service
@RequiredArgsConstructor
class ScreeningEventHandlerService {

    private final RoomRepository roomRepository;

    @TransactionalEventListener(phase = TransactionPhase.BEFORE_COMMIT)
    public void handle(ScreeningCreatedEvent event) {
        roomRepository
                .readById(event.roomId())
                .orElseThrow(() -> new EntityNotFoundException("Room"))
                .addOccupation(event.start(), event.end());
    }

    @TransactionalEventListener(phase = TransactionPhase.BEFORE_COMMIT)
    public void handle(ScreeningEndedEvent event) {
        roomRepository
                .readById(event.roomId())
                .orElseThrow(() -> new EntityNotFoundException("Room"))
                .removeOccupation(event.start(), event.end());
    }
}
