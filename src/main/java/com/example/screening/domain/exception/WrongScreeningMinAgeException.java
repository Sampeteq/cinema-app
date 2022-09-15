package com.example.screening.domain.exception;

import static com.example.screening.domain.ScreeningValues.SCREENING_MAX_AGE;
import static com.example.screening.domain.ScreeningValues.SCREENING_MIN_AGE;

public class WrongScreeningMinAgeException extends ScreeningException {

    public WrongScreeningMinAgeException() {
        super(
                "Wrong screening min age exception"
                + ".Min: " + SCREENING_MIN_AGE
                + ".Max: " + SCREENING_MAX_AGE
        );
    }
}
