package com.cinema.tickets.application;

import com.cinema.screenings.domain.events.ScreeningCreatedEvent;
import com.cinema.tickets.domain.Seat;
import com.cinema.tickets.domain.SeatStatus;
import com.cinema.tickets.domain.repositories.SeatRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Component;

import java.util.List;
import java.util.stream.IntStream;

@Component("Seats - ScreeningCreatedHandler")
@RequiredArgsConstructor
public class ScreeningCreatedHandler {

    private final SeatRepository seatRepository;

    public void handle(ScreeningCreatedEvent event) {
        createSeats(
                event.room().rowsNumber(),
                event.room().seatsNumberInOneRow(),
                event.screeningId()
        ).forEach(seatRepository::add);
    }

    private List<Seat> createSeats(int rowsNumber, int seatsNumberInOneRow, Long screeningId) {
        return IntStream
                .rangeClosed(1, rowsNumber)
                .boxed()
                .flatMap(rowNumber -> IntStream
                        .rangeClosed(1, seatsNumberInOneRow)
                        .mapToObj(seatNumber -> new Seat(rowNumber, seatNumber, SeatStatus.FREE, screeningId))
                )
                .toList();
    }
}
