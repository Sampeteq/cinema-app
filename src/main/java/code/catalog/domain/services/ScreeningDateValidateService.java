package code.catalog.domain.services;

import code.catalog.domain.exceptions.ScreeningWrongDateException;
import org.springframework.stereotype.Component;

import java.time.LocalDateTime;

@Component
public class ScreeningDateValidateService {

    public void validate(LocalDateTime screeningDate, LocalDateTime currentDate) {
        var currentYear = currentDate.getYear();
        var screeningDateYear = screeningDate.getYear();
        var isYearCurrentOrNextOne = screeningDateYear == currentYear || screeningDateYear == currentYear + 1;
        if (!isYearCurrentOrNextOne) {
            throw new ScreeningWrongDateException();
        }
    }
}