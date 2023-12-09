package com.cinema.halls;

import com.cinema.halls.domain.Hall;
import com.cinema.halls.infrastructure.config.ConfigHallDto;

public class HallFixture {
    public static final int HALL_ROWS_NUMBER = 10;
    public static final int HALL_ROW_SEATS_NUMBER = 15;

    public static Hall createHall() {
        return new Hall(SeatFixture.createSeats());
    }

    public static ConfigHallDto createCreateHallCommand() {
        return new ConfigHallDto(
                HALL_ROWS_NUMBER,
                HALL_ROW_SEATS_NUMBER
        );
    }
}
