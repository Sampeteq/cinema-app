package code.screening.exception;

import java.time.Year;

public class ScreeningYearException extends ScreeningException {

    public ScreeningYearException(Year given, Year current) {
        super(
                "A screening's year must be current or next one"
                        + ".Given: " + given
                        + ".Current: " + current
                        + ".Next one: " + current.plusYears(1), ScreeningExceptionType.DATE
        );
    }
}
