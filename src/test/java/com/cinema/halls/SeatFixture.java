package com.cinema.halls;

import com.cinema.halls.domain.Seat;

import java.util.List;

public class SeatFixture {

    private SeatFixture() {}

    public static List<Seat> createSeats(Long hallId) {
        return List.of(
                new Seat(1, 1, hallId),
                new Seat(1, 2, hallId)
        );
    }
}
