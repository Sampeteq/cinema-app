package com.cinema.halls;

import com.cinema.halls.application.commands.CreateHall;
import com.cinema.halls.application.commands.CreateSeatDto;
import com.cinema.halls.domain.Hall;

import java.util.List;

public class HallFixture {

    public static Hall createHall() {
        return new Hall(HallSeatFixture.createSeats());
    }

    public static CreateHall createCreateHallCommand() {
       return new CreateHall(
               List.of(
                       new CreateSeatDto(1, 1),
                       new CreateSeatDto(1, 2)
               )
       );
    }
}
