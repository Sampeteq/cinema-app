package com.cinema.halls.infrastructure.config;

import com.cinema.halls.domain.Hall;
import com.cinema.halls.domain.HallRepository;
import com.cinema.halls.domain.SeatFactory;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Component;

@Component
@RequiredArgsConstructor
@Slf4j
public class CreateHallService {

    private final SeatFactory seatFactory;
    private final HallRepository hallRepository;

    public void handle(ConfigHallDto command) {
        var seats = seatFactory.createSeats(
                command.rowsNumber(),
                command.seatsNumberInOneRow()
                );
        var hall = new Hall(
                command.seatsNumberInOneRow(),
                command.rowsNumber(),
                seats
        );
        hallRepository.add(hall);
    }
}
