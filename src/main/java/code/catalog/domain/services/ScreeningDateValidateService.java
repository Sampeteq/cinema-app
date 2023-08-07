package code.catalog.domain.services;

import code.catalog.domain.exceptions.ScreeningDateInPastException;
import code.catalog.domain.exceptions.ScreeningDateOutOfRangeException;
import org.springframework.stereotype.Component;

import java.time.Duration;
import java.time.LocalDateTime;

@Component
public class ScreeningDateValidateService {

    private static final int MIN_BOOKING_DAYS_NUMBER = 7;
    private static final int MAX_BOOKING_DAYS_NUMBER = 21;

    public void validate(LocalDateTime screeningDate, LocalDateTime currentDate) {
       if (screeningDate.isBefore(currentDate)) {
           throw new ScreeningDateInPastException();
       }
       var currentAndScreeningDateDifference = Duration
                .between(screeningDate, currentDate)
                .abs()
                .toDays();
       var isScreeningDateOutOfRange =
               currentAndScreeningDateDifference < MIN_BOOKING_DAYS_NUMBER
                       || currentAndScreeningDateDifference > MAX_BOOKING_DAYS_NUMBER;
       if (isScreeningDateOutOfRange) {
           throw new ScreeningDateOutOfRangeException();
       }
    }
}