package code.screenings.exception;

public class ScreeningYearException extends ScreeningException {

    public ScreeningYearException(int given, int current) {
        super(
                "A screening's year must be current or next one"
                        + ".Given: " + given
                        + ".Current: " + current
                        + ".Next one: " + current + 1
        );
    }
}
