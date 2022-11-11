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

    private static final int CURRENT_YEAR = Year.now().getValue();

    private LocalDateTime value;

    static ScreeningDate of(LocalDateTime date) {
        if (date.getYear() != CURRENT_YEAR && date.getYear() != CURRENT_YEAR + 1) {
            throw new ScreeningYearException(date.getYear());
        } else {
            return new ScreeningDate(date);
        }
    }

    int differenceBetweenCurrentDateAndScreeningOneInHours(Clock clock) {
        return (int) Duration
                .between(LocalDateTime.now(clock), this.value)
                .abs()
                .toHours();
    }
}
