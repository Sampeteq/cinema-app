package com.cinema.screenings.application.handlers;

import com.cinema.screenings.application.queries.SeatExists;
import com.cinema.screenings.domain.ScreeningRepository;
import com.cinema.screenings.domain.exceptions.ScreeningNotFoundException;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Component;

@Component
@RequiredArgsConstructor
@Slf4j
public class SeatExistsHandler {

    private final ScreeningRepository screeningRepository;

    public boolean handle(SeatExists query) {
        log.info("Query:{}", query);
        return screeningRepository
                .readById(query.screeningId())
                .orElseThrow(ScreeningNotFoundException::new)
                .hasSeat(query.seatId());
    }
}
