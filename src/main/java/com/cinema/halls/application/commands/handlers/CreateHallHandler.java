package com.cinema.halls.application.commands.handlers;

import com.cinema.halls.application.commands.CreateHall;
import com.cinema.halls.domain.Hall;
import com.cinema.halls.domain.HallRepository;
import com.cinema.halls.domain.exceptions.HallIdAlreadyExistsException;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Component;

@Component
@RequiredArgsConstructor
@Slf4j
public class CreateHallHandler {

    private final HallRepository hallRepository;

    public void handle(CreateHall command) {
        if (hallRepository.existsById(command.id())) {
            throw new HallIdAlreadyExistsException();
        }
        var hall = new Hall(
                command.id(),
                command.seatsNumberInOneRow(),
                command.rowsNumber()
        );
        hallRepository.add(hall);
    }
}
