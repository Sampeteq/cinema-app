package code.catalog.domain.services;

import code.catalog.domain.exceptions.ScreeningDateException;
import org.springframework.stereotype.Component;

import java.time.Duration;
import java.time.LocalDateTime;

@Component
public class ScreeningDateValidateService {

    private static final int bookingDaysNumber = 7;

    public void validate(LocalDateTime screeningDate, LocalDateTime currentDate) {
       if (screeningDate.isBefore(currentDate)) {
           throw new ScreeningDateException("Screening date cannot be earlier than current");
       }
       var currentAndScreeningDateDifference = Duration
                .between(screeningDate, currentDate)
                .abs()
                .toDays();
       if (currentAndScreeningDateDifference < 7) {
           throw new ScreeningDateException(
                   "Difference between current and screening date " +
                           "cannot be below " + bookingDaysNumber + " days"
           );
       }
    }
}