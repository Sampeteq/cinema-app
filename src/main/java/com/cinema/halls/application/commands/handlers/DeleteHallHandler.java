package com.cinema.halls.application.commands.handlers;

import com.cinema.halls.application.commands.DeleteHall;
import com.cinema.halls.domain.HallRepository;
import com.cinema.halls.domain.exceptions.HallNotFoundException;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Component;

@Component
@RequiredArgsConstructor
@Slf4j
public class DeleteHallHandler {

    private final HallRepository hallRepository;

    public void handle(DeleteHall command) {
        log.info("Command:{}", command);
        var hall = hallRepository
                .getById(command.hallId())
                .orElseThrow(HallNotFoundException::new);
        log.info("Found hall:{}", hall);
        hallRepository.delete(hall);
        log.info("Hall deleted");
    }
}
