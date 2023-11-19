package com.cinema.halls.application;

import com.cinema.halls.application.queries.GetFirstAvailableHall;
import com.cinema.halls.application.queries.dto.HallDto;
import com.cinema.halls.application.queries.handlers.GetFirstAvailableHallHandler;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Component;

import java.time.LocalDateTime;

@Component
@RequiredArgsConstructor
public class HallApi {

    private final GetFirstAvailableHallHandler getFirstAvailableHallHandler;

    public HallDto getFirstAvailableHall(LocalDateTime from, LocalDateTime to) {
        return getFirstAvailableHallHandler.handle(new GetFirstAvailableHall(from, to));
    }
}
