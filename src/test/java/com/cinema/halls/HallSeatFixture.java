package com.cinema.halls;

import com.cinema.halls.domain.HallSeat;

import java.util.List;

public class HallSeatFixture {

    private HallSeatFixture() {}

    public static List<HallSeat> createSeats() {
        return List.of(
                new HallSeat(1, 1),
                new HallSeat(1, 2)
        );
    }
}
