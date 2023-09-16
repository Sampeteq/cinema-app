package com.cinema.catalog.domain.services;

import com.cinema.catalog.domain.exceptions.ScreeningDateInPastException;
import com.cinema.catalog.domain.exceptions.ScreeningDateOutOfRangeException;
import org.springframework.stereotype.Component;

import java.time.Duration;
import java.time.LocalDateTime;

@Component
public class ScreeningDateValidateService {

    private static final int MIN_BOOKING_DAYS = 7;
    private static final int MAX_BOOKING_DAYS = 21;

    public void validate(LocalDateTime screeningDate, LocalDateTime currentDate) {
       if (screeningDate.isBefore(currentDate)) {
           throw new ScreeningDateInPastException();
       }
       var datesDifference = Duration
                .between(screeningDate, currentDate)
                .abs()
                .toDays();
       var isDateOutOfRange = datesDifference < MIN_BOOKING_DAYS || datesDifference > MAX_BOOKING_DAYS;
       if (isDateOutOfRange) {
           throw new ScreeningDateOutOfRangeException();
       }
    }
}