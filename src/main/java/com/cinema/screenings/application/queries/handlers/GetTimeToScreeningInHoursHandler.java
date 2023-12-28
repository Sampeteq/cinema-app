package com.cinema.screenings.application.queries.handlers;

import com.cinema.screenings.application.queries.GetTimeToScreeningInHours;
import com.cinema.screenings.domain.ScreeningRepository;
import com.cinema.screenings.domain.exceptions.ScreeningNotFoundException;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Component;

import java.time.Clock;
import java.time.Duration;
import java.time.LocalDateTime;

@Component
@RequiredArgsConstructor
@Slf4j
public class GetTimeToScreeningInHoursHandler {

    private final ScreeningRepository screeningRepository;
    private final Clock clock;

    public long handle(GetTimeToScreeningInHours query) {
        log.info("Query:{}", query);
        var screeningDate = screeningRepository
                .getById(query.screeningId())
                .orElseThrow(ScreeningNotFoundException::new)
                .getDate();
        return timeToScreeningInHours(clock, screeningDate);
    }

    private long timeToScreeningInHours(Clock clock, LocalDateTime screeningDate) {
        var currentDate = LocalDateTime.now(clock);
        return Duration
                .between(currentDate, screeningDate)
                .abs()
                .toHours();
    }
}
