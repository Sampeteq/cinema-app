package code.screenings;

import code.screenings.exception.ScreeningYearException;
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

    static ScreeningDate of(LocalDateTime date) {
        if (isScreeningYearCurrentOrNextOne(date.getYear())) {
            return new ScreeningDate(date);
        } else {
            throw new ScreeningYearException(date.getYear());
        }
    }

    int timeToScreeningStart(Clock clock) {
        return (int) Duration
                .between(LocalDateTime.now(clock), this.value)
                .abs()
                .toHours();
    }

    private static boolean isScreeningYearCurrentOrNextOne(int year) {
        var currentYear = Year.now().getValue();
        return year == currentYear || year == currentYear + 1;
    }
}
