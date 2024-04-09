package com.cinema.halls;

import com.cinema.halls.domain.Hall;
import com.cinema.halls.domain.Seat;

import java.util.List;
import java.util.UUID;

public class HallFixtures {

    public static Hall createHall() {
        return new Hall(
                UUID.randomUUID(),
                List.of(
                        new Seat(1, 1),
                        new Seat(1, 2)
                )
        );
    }
}
