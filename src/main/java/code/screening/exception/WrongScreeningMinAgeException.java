package code.screening.exception;

import code.screening.ScreeningValues;

public class WrongScreeningMinAgeException extends ScreeningException {

    public WrongScreeningMinAgeException() {
        super(
                "Wrong screening min age exception"
                        + ".Min: " + ScreeningValues.SCREENING_MIN_AGE
                        + ".Max: " + ScreeningValues.SCREENING_MAX_AGE
        );
    }
}
