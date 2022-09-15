package com.example.screening.domain.exception;

import java.time.Year;

public class WrongScreeningYearException extends ScreeningException {

    public WrongScreeningYearException(Year given, Year current) {
        super(
                "A new screening's year must be current or next one"
                        + ".Given: "
                        + given + ".Current: "
                        + current
                        + ".Next one: "
                        + current.getValue() + 1
        );
    }
}
