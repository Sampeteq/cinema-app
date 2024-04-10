package com.cinema.halls;

import com.cinema.halls.domain.Hall;
import com.cinema.halls.domain.Seat;
import com.cinema.halls.infrastructure.ui.HallCreateDto;

import java.util.List;
import java.util.UUID;

public class HallFixtures {

    private static final List<Seat> seats = List.of(
            new Seat(1, 1),
            new Seat(1, 2)
    );

    public static HallCreateDto createHallCreateDto() {
        return new HallCreateDto(seats);
    }

    public static Hall createHall() {
        return new Hall(
                UUID.randomUUID(),
                seats
        );
    }
}
