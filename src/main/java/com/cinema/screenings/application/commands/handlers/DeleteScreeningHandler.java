package com.cinema.screenings.application.commands.handlers;

import com.cinema.screenings.application.commands.DeleteScreening;
import com.cinema.screenings.domain.ScreeningRepository;
import com.cinema.screenings.domain.exceptions.ScreeningNotFoundException;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Component;

@Component
@RequiredArgsConstructor
@Slf4j
public class DeleteScreeningHandler {

    private final ScreeningRepository screeningRepository;

    public void handle(DeleteScreening command) {
        log.info("Command:{}", command);
        var screening = screeningRepository
                .readById(command.screeningId())
                .orElseThrow(ScreeningNotFoundException::new);
        screeningRepository.delete(screening);
    }
}
