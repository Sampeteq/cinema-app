package com.cinema.screenings.application.handlers;

import com.cinema.screenings.application.queries.ReadScreeningDate;
import com.cinema.screenings.domain.ScreeningRepository;
import com.cinema.screenings.domain.exceptions.ScreeningNotFoundException;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Component;

import java.time.LocalDateTime;

@Component
@RequiredArgsConstructor
@Slf4j
public class ReadScreeningDateHandler {

    private final ScreeningRepository screeningRepository;

    public LocalDateTime handle(ReadScreeningDate query) {
        log.info("Query:{}", query);
        return screeningRepository
                .readById(query.screeningId())
                .orElseThrow(ScreeningNotFoundException::new)
                .getDate();
    }
}
