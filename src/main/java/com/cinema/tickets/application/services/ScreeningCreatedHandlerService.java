package com.cinema.tickets.application.services;

import com.cinema.tickets.domain.Screening;
import com.cinema.tickets.domain.Seat;
import com.cinema.tickets.domain.ports.ScreeningRepository;
import com.cinema.catalog.domain.events.ScreeningCreatedEvent;
import lombok.RequiredArgsConstructor;
import org.springframework.context.event.EventListener;
import org.springframework.stereotype.Service;

import java.util.List;

import static java.util.stream.IntStream.rangeClosed;

@Service
@RequiredArgsConstructor
class ScreeningCreatedHandlerService {

    private final ScreeningRepository screeningRepository;

    @EventListener(ScreeningCreatedEvent.class)
    void handle(ScreeningCreatedEvent event) {
        var seats = createSeats(event.rowsQuantity(), event.seatsQuantityInOneRow());
        var screening = Screening.create(
                event.id(),
                event.date(),
                event.filmTitle(),
                event.roomCustomId(),
                seats
        );
        screeningRepository.add(screening);
    }

    private List<Seat> createSeats(int rowsQuantity, int seatsQuantityInOneRow) {
        return rangeClosed(1, rowsQuantity)
                .boxed()
                .flatMap(rowNumber -> rangeClosed(1, seatsQuantityInOneRow)
                        .mapToObj(seatNumber -> Seat.create(rowNumber, seatNumber))
                )
                .toList();
    }
}
