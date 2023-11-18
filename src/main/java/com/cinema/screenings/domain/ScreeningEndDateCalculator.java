package com.cinema.screenings.domain;

import org.springframework.stereotype.Component;

import java.time.LocalDateTime;

@Component
public class ScreeningEndDateCalculator {

    public LocalDateTime calculateEndDate(LocalDateTime screeningDate, int filmDurationInMinutes) {
        return screeningDate.plusMinutes(filmDurationInMinutes);
    }
}
