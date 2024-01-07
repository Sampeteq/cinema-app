package com.cinema.halls;

import com.cinema.halls.application.dto.CreateHallDto;
import com.cinema.halls.application.dto.CreateSeatDto;
import com.cinema.halls.domain.Hall;
import com.cinema.halls.domain.HallSeat;

import java.util.List;

public class HallFixture {

    public static Hall createHall() {
        return new Hall(
                List.of(
                        new HallSeat(1, 1),
                        new HallSeat(1, 2)
                )
        );
    }

    public static CreateHallDto createCreateHallDto() {
        return new CreateHallDto(
                List.of(
                        new CreateSeatDto(1, 1),
                        new CreateSeatDto(1, 2)
                )
        );
    }
}
