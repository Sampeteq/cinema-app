package com.cinema.tickets;

import com.cinema.tickets.domain.Seat;
import com.cinema.tickets.domain.SeatStatus;

import java.util.List;

public class SeatFixture {

    private SeatFixture() {}

    public static List<Seat> createSeats(Long screeningId) {
        return List.of(
                new Seat(1, 1, SeatStatus.FREE, screeningId),
                new Seat(1, 2, SeatStatus.FREE, screeningId)
        );
    }
}
