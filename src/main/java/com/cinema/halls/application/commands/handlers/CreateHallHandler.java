package com.cinema.halls.application.commands.handlers;

import com.cinema.halls.application.commands.CreateHall;
import com.cinema.halls.domain.Hall;
import com.cinema.halls.domain.HallRepository;
import com.cinema.halls.domain.HallSeat;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Component;

@Component
@RequiredArgsConstructor
@Slf4j
public class CreateHallHandler {

    private final HallRepository hallRepository;

    public void handle(CreateHall command) {
        log.info("Command:{}", command);
        var seats = command.seats()
                .stream()
                .map(createSeatDto -> new HallSeat(createSeatDto.rowNumber(), createSeatDto.number()))
                .toList();
        var hall = new Hall(seats);
        hallRepository.add(hall);
        log.info("Hall added");
    }
}
