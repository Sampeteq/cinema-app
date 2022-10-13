package code.screening.exception;

import code.screening.ScreeningValues;

public class ScreeningMinAgeException extends ScreeningException {

    public ScreeningMinAgeException() {
        super(
                "Wrong screening min age exception"
                        + ".Min: " + ScreeningValues.SCREENING_MIN_AGE
                        + ".Max: " + ScreeningValues.SCREENING_MAX_AGE
        );
    }
}
