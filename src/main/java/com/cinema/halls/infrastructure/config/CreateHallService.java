package com.cinema.halls.infrastructure.config;

import com.cinema.halls.domain.Hall;
import com.cinema.halls.domain.HallRepository;
import com.cinema.halls.domain.exceptions.HallIdAlreadyExistsException;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Component;

@Component
@RequiredArgsConstructor
@Slf4j
public class CreateHallService {

    private final HallRepository hallRepository;

    public void handle(ConfigHallDto command) {
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
