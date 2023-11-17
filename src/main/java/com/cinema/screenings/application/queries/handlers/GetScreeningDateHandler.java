package com.cinema.screenings.application.queries.handlers;

import com.cinema.screenings.application.queries.GetScreeningDate;
import com.cinema.screenings.domain.ScreeningRepository;
import com.cinema.screenings.domain.exceptions.ScreeningNotFoundException;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Component;

import java.time.LocalDateTime;

@Component
@RequiredArgsConstructor
@Slf4j
public class GetScreeningDateHandler {

    private final ScreeningRepository screeningRepository;

    public LocalDateTime handle(GetScreeningDate query) {
        log.info("Query:{}", query);
        return screeningRepository
                .getById(query.screeningId())
                .orElseThrow(ScreeningNotFoundException::new)
                .getDate();
    }
}
