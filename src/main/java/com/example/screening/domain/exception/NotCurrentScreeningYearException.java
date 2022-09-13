package com.example.screening.domain.exception;

import java.time.Year;

public class NotCurrentScreeningYearException extends RuntimeException {

    public NotCurrentScreeningYearException(Year given, Year current) {
        super("A new screening's year must be same as current one.Given: " + given + ".Current: " + current);
    }
}
