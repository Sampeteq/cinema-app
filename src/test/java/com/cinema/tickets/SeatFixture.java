package com.cinema.tickets;

import com.cinema.tickets.domain.Seat;

import java.util.List;

public class SeatFixture {

    private SeatFixture() {}

    public static List<Seat> createSeats(Long screeningId) {
        return List.of(
                new Seat(1, 1, screeningId),
                new Seat(1, 2, screeningId)
        );
    }
}
