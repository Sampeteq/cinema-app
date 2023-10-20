package com.cinema.screenings.domain.exceptions;

public class ScreeningNotFoundException extends RuntimeException {

    public ScreeningNotFoundException() {
        super("Screening not found");
    }
}
