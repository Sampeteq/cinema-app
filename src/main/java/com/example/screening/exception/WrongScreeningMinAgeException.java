package com.example.screening.exception;

import static com.example.screening.ScreeningValues.SCREENING_MAX_AGE;
import static com.example.screening.ScreeningValues.SCREENING_MIN_AGE;

public class WrongScreeningMinAgeException extends ScreeningException {

    public WrongScreeningMinAgeException() {
        super(
                "Wrong screening min age exception"
                        + ".Min: " + SCREENING_MIN_AGE
                        + ".Max: " + SCREENING_MAX_AGE
        );
    }
}
