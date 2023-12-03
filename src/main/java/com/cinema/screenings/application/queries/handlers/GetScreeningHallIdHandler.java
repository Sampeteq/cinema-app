package com.cinema.screenings.application.queries.handlers;

import com.cinema.screenings.application.queries.GetScreeningHallId;
import com.cinema.screenings.domain.ScreeningRepository;
import com.cinema.screenings.domain.exceptions.ScreeningNotFoundException;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Component;

@Component
@RequiredArgsConstructor
@Slf4j
public class GetScreeningHallIdHandler {

    private final ScreeningRepository screeningRepository;

    public Long handle(GetScreeningHallId query) {
        log.info("Query:{}", query);
        return screeningRepository
                .getById(query.screeningId())
                .orElseThrow(ScreeningNotFoundException::new)
                .getHallId();
    }
}
