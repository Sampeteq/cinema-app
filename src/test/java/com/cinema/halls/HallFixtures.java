package com.cinema.halls;

import java.util.List;

public class HallFixtures {

    public static Hall createHall() {
        return new Hall(
                List.of(
                        new Seat(1, 1),
                        new Seat(1, 2)
                )
        );
    }
}
