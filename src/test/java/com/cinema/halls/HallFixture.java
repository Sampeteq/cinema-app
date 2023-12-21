package com.cinema.halls;

import com.cinema.halls.domain.Hall;
import com.cinema.halls.infrastructure.config.ConfigHallDto;
import com.cinema.halls.infrastructure.config.ConfigSeatDto;

import java.util.List;

public class HallFixture {

    public static Hall createHall() {
        return new Hall(HallSeatFixture.createSeats());
    }

    public static ConfigHallDto createCreateHallCommand() {
       return new ConfigHallDto(
               List.of(new ConfigSeatDto(1, 1), new ConfigSeatDto(1, 2))
       );
    }
}
