package code.screening;

import code.screening.exception.ScreeningYearException;
import lombok.AccessLevel;
import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;

import javax.persistence.Embeddable;
import java.time.Clock;
import java.time.Duration;
import java.time.LocalDateTime;
import java.time.Year;

@Embeddable
@NoArgsConstructor(access = AccessLevel.PROTECTED)
@AllArgsConstructor(access = AccessLevel.PRIVATE)
@Getter
class ScreeningDate {

    private LocalDateTime value;

    static ScreeningDate of(LocalDateTime date, Year currentYear) {
        if (date.getYear() != currentYear.getValue() && date.getYear() != currentYear.getValue() + 1) {
            throw new ScreeningYearException(Year.of(date.getYear()), currentYear);
        } else {
            return new ScreeningDate(date);
        }
    }
}
