package com.cinema.screenings.application;

import com.cinema.screenings.application.queries.GetScreeningDate;
import com.cinema.screenings.application.queries.handlers.GetScreeningDateHandler;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Component;

import java.time.LocalDateTime;

@Component
@RequiredArgsConstructor
public class ScreeningApi {

    private final GetScreeningDateHandler getScreeningDateHandler;

    public LocalDateTime getScreeningDate(Long screeningId) {
        return getScreeningDateHandler.handle(new GetScreeningDate(screeningId));
    }
}
