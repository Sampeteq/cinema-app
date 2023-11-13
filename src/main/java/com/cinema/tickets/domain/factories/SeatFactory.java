package com.cinema.tickets.domain.factories;

import com.cinema.tickets.domain.Seat;
import com.cinema.tickets.domain.SeatStatus;
import org.springframework.stereotype.Component;

import java.util.List;
import java.util.stream.IntStream;

@Component
public class SeatFactory {

    public List<Seat> createSeats(int rowsNumber, int seatsNumberInOneRow, Long screeningId) {
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
