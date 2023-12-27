package com.cinema.halls.application.queries.handlers;

import com.cinema.halls.application.queries.GetFirstAvailableHall;
import com.cinema.halls.application.queries.dto.HallDto;
import com.cinema.halls.domain.HallRepository;
import com.cinema.halls.domain.exceptions.HallsNoAvailableException;
import com.cinema.halls.infrastructure.HallMapper;
import com.cinema.screenings.application.queries.GetScreenings;
import com.cinema.screenings.application.queries.dto.ScreeningDto;
import com.cinema.screenings.application.queries.handlers.GetScreeningsHandler;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Component;

import java.time.LocalDateTime;

@Component
@RequiredArgsConstructor
@Slf4j
public class GetFirstAvailableHallHandler {

    private final HallRepository hallRepository;
    private final HallMapper hallMapper;
    private final GetScreeningsHandler getScreeningsHandler;

    public HallDto handle(GetFirstAvailableHall query) {
        log.info("Query:{}", query);
        var screenings = getScreeningsHandler.handle(GetScreenings.builder().build());
        return hallRepository
                .getAll()
                .stream()
                .filter(hall -> screenings
                        .stream()
                        .filter(screening -> screening.hallId().equals(hall.getId()))
                        .noneMatch(screening -> hasCollision(query.start(), query.end(), screening))
                )
                .findFirst()
                .map(hallMapper::toDto)
                .orElseThrow(HallsNoAvailableException::new);
    }

    private boolean hasCollision(
            LocalDateTime startAt,
            LocalDateTime endAt,
            ScreeningDto dto) {
        var startCollision = !startAt.isBefore(dto.date()) && !startAt.isAfter(dto.endDate());
        var endCollision = !endAt.isBefore(dto.date()) && !endAt.isAfter(dto.endDate());
        return startCollision || endCollision;
    }
}
