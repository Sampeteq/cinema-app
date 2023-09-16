package com.cinema.catalog.domain.services;

import com.cinema.catalog.domain.exceptions.ScreeningDateInPastException;
import com.cinema.catalog.domain.exceptions.ScreeningDateOutOfRangeException;
import org.springframework.stereotype.Component;

import java.time.Duration;
import java.time.LocalDateTime;

@Component
public class ScreeningDateValidateService {

    private static final int MIN_DAYS_NUMBER = 7;
    private static final int MAX_DAYS_NUMBER = 21;

    public void validate(LocalDateTime screeningDate, LocalDateTime currentDate) {
       if (screeningDate.isBefore(currentDate)) {
           throw new ScreeningDateInPastException();
       }
       var datesDifference = Duration
                .between(screeningDate, currentDate)
                .abs()
                .toDays();
       var isDateOutOfRange = datesDifference < MIN_DAYS_NUMBER || datesDifference > MAX_DAYS_NUMBER;
       if (isDateOutOfRange) {
           throw new ScreeningDateOutOfRangeException();
       }
    }
}