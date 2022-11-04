package code.screenings.exception;

import java.time.Year;

public class ScreeningYearException extends ScreeningException {

    private static final int current_year = Year.now().getValue();

    public ScreeningYearException(int givenYear) {
        super(
                "A screening's year must be current or next one"
                        + ".Given: " + givenYear
                        + ".Current: " + current_year
                        + ".Next one: " + current_year + 1
        );
    }
}
