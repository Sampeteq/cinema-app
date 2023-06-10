package code.films.domain;

import code.films.domain.exceptions.FilmScreeningWrongDateException;
import org.springframework.stereotype.Component;

import java.time.Clock;
import java.time.LocalDateTime;
import java.time.ZoneOffset;

@Component
public class FilmScreeningDateValidator {

    public void validate(LocalDateTime date, Clock clock) {
        var currentDate = LocalDateTime.ofInstant(clock.instant(), ZoneOffset.UTC);
        var currentYear = currentDate.getYear();
        var isYearCurrentOrNextOne = date.getYear() == currentYear || date.getYear() == currentYear + 1;
        if (!isYearCurrentOrNextOne) {
            throw new FilmScreeningWrongDateException();
        }
    }
}