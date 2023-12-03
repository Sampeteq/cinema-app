package com.cinema.halls.infrastructure.config;

import com.cinema.halls.domain.Hall;
import com.cinema.halls.domain.HallRepository;
import com.cinema.halls.domain.SeatFactory;
import com.cinema.halls.domain.SeatRepository;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Component;

@Component
@RequiredArgsConstructor
@Slf4j
public class CreateHallService {

    private final HallRepository hallRepository;
    private final SeatFactory seatFactory;
    private final SeatRepository seatRepository;

    public void handle(ConfigHallDto command) {
        var hall = new Hall(
                command.seatsNumberInOneRow(),
                command.rowsNumber()
        );
        var addedHall = hallRepository.add(hall);
        seatFactory.createSeats(
                addedHall.getRowsNumber(),
                addedHall.getSeatsNumberInOneRow(),
                addedHall.getId()
        ).forEach(seatRepository::add);
    }
}
