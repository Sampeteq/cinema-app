package code.screening;

import code.screening.exception.WrongScreeningYearException;
import lombok.AccessLevel;
import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;

import javax.persistence.Embeddable;
import java.time.LocalDateTime;
import java.time.Year;

@Embeddable
@NoArgsConstructor(access = AccessLevel.PROTECTED)
@AllArgsConstructor(access = AccessLevel.PRIVATE)
@Getter
public class ScreeningDate {

    private LocalDateTime value;

    public static ScreeningDate of(LocalDateTime value, Year currentYear) {
        if (value.getYear() != currentYear.getValue()) {
            throw new WrongScreeningYearException(Year.of(value.getYear()), currentYear);
        } else {
            return new ScreeningDate(value);
        }
    }
}
